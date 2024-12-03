import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { finalize } from 'rxjs/operators'; // add new
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT } from 'app/config/navigation.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import { UserManagementDeleteDialogComponent } from '../delete/user-management-delete-dialog.component';
import { UserFilterDTO } from '../userFilterDTO.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {swalClose, swalOnLoading, swalSuccessWithMessage} from '../../../shared/swal-common/swal-common';
import {CustomValidator} from "../../../validators/custom-validator";
import { CommonUtil } from '../../../shared/util/common-util';
import {DANGER_COLOR, PRIMARY_COLOR} from "../../../config/color.code.constant";
import {SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT} from "../../../shared/swal-common/swal.properties.constant";

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent implements OnInit {
  currentAccount: Account | null = null;
  users: User[] | null = null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  resetLoading = false;
  private username: string;



  // ++++++++++++++++
  failedAttemptsCount: number = 0; // To store the count of failed attempts
  isButtonActive: boolean = false;
  // ++++++++++++++++++++++++

  userfilterDTO = new UserFilterDTO();
  authorities: string[] = [];

  doNotMatch = false;
  error = false;
  success = false;
  selectedAccount?: User;
  passwordForm = new FormGroup({
    newPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

  constructor(
    private userService: UserManagementService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.handleNavigation();
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(_index: number, item: User): number {
    return item.id!;
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading = true;
    this.userService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers);
        },
        error: () => (this.isLoading = false),
      });
  }

  transition(): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: `${this.predicate},${this.ascending ? ASC : DESC}`,
      },
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      this.predicate = sort[0];
      this.ascending = sort[1] === ASC;
      this.loadAll();
    });
  }

  private sort(): string[] {
    const result = [`${this.predicate},${this.ascending ? ASC : DESC}`];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(users: User[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.users = users;
  }

  searchText = new FormControl('', {
    validators: [CustomValidator.naturalTextValidator(true)]
  })
  isSearchTextInvalid = false;
  reloadDataAsSearchFilter(): void {
    // if(CommonUtil.isNullOrEmpty(this.userfilterDTO.searchText)){
    //   this.isSearchTextInvalid = false;
    //   return;
    // }
    // if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.userfilterDTO.searchText)){
    //   this.isSearchTextInvalid = true;
    //   return;
    // }
    // else this.isSearchTextInvalid = false;

    this.userService
      .querySearch(this.userfilterDTO, {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers);
        },
        error: () => (this.isLoading = false),
      });
  }

  onSearchTextChange(): void {
    if(this.searchText.errors?.pattern){
      return;
    }
    this.userfilterDTO.searchText = this.searchText.value;
    this.reloadDataAsSearchFilter();
  }

  onRoleChange(): void {
    this.reloadDataAsSearchFilter();
  }

  showPasswordChangeModal(passwordModal: any, user: User) {
    this.selectedAccount = user;
    this.modalService.open(passwordModal);
  }

  changePassword(): void {
    this.error = false;
    this.success = false;
    this.doNotMatch = false;

    const { newPassword, confirmPassword } = this.passwordForm.getRawValue();
    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      this.userService.changePassword(this.selectedAccount.login, newPassword).subscribe({
        next: () => {
          this.passwordForm.reset();
          this.modalService.dismissAll();
          swalSuccessWithMessage('Password changed successfully');
        },
        error: () => (this.error = true),
      });
    }
  }


  // resetFailedAttempt(username : string) {
  //
  //   Swal.fire({
  //     text: "Do you want to reset count",
  //     showCancelButton: true,
  //     confirmButtonColor: PRIMARY_COLOR,
  //     cancelButtonColor: DANGER_COLOR,
  //     confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
  //     cancelButtonText: SWAL_CANCEL_BTN_TEXT,
  //   }).then(result => {
  //
  //     if (result.isConfirmed) {
  //       swalOnLoading('Downloading');
  //       // @ts-ignore
  //       x => {
  //         const newBlob = new Blob([x], { type: 'application/octet-stream' });
  //         if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
  //           (window.navigator as any).msSaveOrOpenBlob(newBlob);
  //           return;
  //         }
  //         const data = window.URL.createObjectURL(newBlob);
  //
  //         const link = document.createElement('a');
  //         link.href = data;
  //         link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
  //
  //         // tslint:disable-next-line:typedef
  //         setTimeout(function() {
  //           window.URL.revokeObjectURL(data);
  //           link.remove();
  //         }, 100);
  //       },
  //         this.userService.resetFailedAttempts(username)
  //           .pipe(
  //             finalize(() => {
  //               swalClose();
  //               Swal.fire({
  //               })
  //             }))
  //           .subscribe({
  //             next: () => {
  //               Swal.fire({ text: "Reset successful" });
  //             },
  //             error: (err: any) => {
  //             }
  //           });
  //     }
  //   });
  // }


  // findContinuousFailedAttemptsZero(username: string): void {
  //   this.userService.findContinuousFailedAttemptsZero(username).subscribe({
  //     next: (response: any) => {
  //       Swal.fire({ text: "Already reset" });
  //     },
  //
  //   });
  //   error: (err: any) => {
  //     console.error("Error:", err);
  //   }
  //
  //
  // }

  ButtonClick(userName: string): void {

    // this.userService.findFailedLoginAttemptWithMultipleFailures(userName).subscribe(
    //   (response) => {
    //
    //     console.log('Multiple failed login attempts check successful');
    //     // this.userService(userName); // Recheck the failed attempts after updating
    //   },
    //   (error) => {
    //     console.error('Error checking failed login attempts:', error);
    //   }
    // );

    // this.findFailedLoginAttemptWithMultipleFailuresccc(userName);
    console.log("LLLLLLLLLLLLLLLLLLLLLLLLLLLLL")
    this.userService.findFailedLoginAttemptByUsername(userName).subscribe(
      (response) => {
        if (response && response.continuousFailedAttempts >= 4) {
          this.isButtonActive = true; // Activate button if failed attempts >= 4
        } else {
          this.isButtonActive = false; // Deactivate button if failed attempts < 4
        }
        this.failedAttemptsCount = response.continuousFailedAttempts;
      },
      (error) => {
        console.error('Error fetching failed login attempts:', error);
      }
    );
  }


  // ++++++++++++++++++++++++++= NEW API +++++++++++++++++++++++++++++++++++++++++++++++

  // findFailedLoginAttemptByUsername(userName :string){
  //   this.userService.findFailedLoginAttemptByUsername(userName).subscribe({
  //     next: (response: any) => {
  //       console.log("1.",response);
  //       },
  //   });
  //   // Swal.fire({
  //   //   text : "rrrrrrrrrrr"
  //   // });
  // }

  // findFailedLoginAttemptWithMultipleFailuresccc(userName :string){
  //   // Swal.fire({
  //   //   text: "Do you want to reset count",
  //   //   showCancelButton: true,
  //   //   confirmButtonColor: PRIMARY_COLOR,
  //   //   cancelButtonColor: DANGER_COLOR,
  //   //   confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
  //   //   cancelButtonText: SWAL_CANCEL_BTN_TEXT,
  //   // })
  //   // swalOnLoading("Please wait ...");
  //   this.userService.findFailedLoginAttemptWithMultipleFailures(userName).subscribe({
  //     next: (response: any) => {
  //       console.log("2",response);
  //       // Swal.fire({ text: "Successful " });
  //     },
  //     error :(err)=>{
  //     console.error("Error:", err);
  //
  //   }
  //   });
  // }


}
