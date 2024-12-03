import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import Swal from 'sweetalert2';
import { IPfNomineeEmployeeDetailsDTO } from '../../../../shared/model/pf-nominee-employee-details.model';
import { IPfAccount } from '../../../pf-account/pf-account.model';
import { IPfNominee } from '../../pf-nominee.model';
import { NomineeType } from '../../../../shared/model/enumerations/nominee-type.model';
import { PfNomineeService } from '../../service/pf-nominee.service';
import { PfAccountService } from '../../../pf-account/service/pf-account.service';
import { NomineeService } from '../../service/nominee.service';
import { NomineeFormService } from '../../update/edit-form-service/nominee-form.service';
import { INominee, Nominee } from '../../nominee.model';
import { PfNomineeFormService } from '../../update/edit-form-service/pf-nominee-form.service';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../../shared/swal-common/swal-common';
import { OrganizationFilesUrl } from 'app/shared/constants/organization-files-url';

@Component({
  selector: 'jhi-gf-nominee-report',
  templateUrl: './gf-nominee-report.component.html',
  styleUrls: ['./gf-nominee-report.component.scss'],
})
export class GfNomineeReportComponent implements OnInit {
  gfNominees!: INominee[];
  nominee!: INominee;
  employeeDetails!: IPfNomineeEmployeeDetailsDTO;
  pfAccounts: IPfAccount[] = [];
  pfAccountIdFromRoute!: number;
  defaultEmptyNominee!: IPfNominee;
  totalUsedPercentage = 0;
  nomineeType!: NomineeType;

  guardianInfo!: INominee;

  editForm = this.fb.group({
    pfAccountId: [],
  });

  pfNomineeForm = this.pfNomineeFormService.createPfNomineeFormGroup();
  nomineeForm = this.nomineeFormService.createNomineeFormGroup();

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected pfNomineeService: PfNomineeService,
    protected pfAccountService: PfAccountService,
    protected router: Router,
    private fb: FormBuilder,
    private nomineeService: NomineeService,
    private pfNomineeFormService: PfNomineeFormService,
    private nomineeFormService: NomineeFormService
  ) {
    this.defaultEmptyNominee = this.pfNomineeFormService.getPfNominee(this.pfNomineeForm);
    this.defaultEmptyNominee.fullName = '';
    this.defaultEmptyNominee.age = 0;
    this.defaultEmptyNominee.sharePercentage = 0.0;

    const nomineeTypeFromRoute = this.activatedRoute.snapshot.data['nomineeType'];

    nomineeTypeFromRoute
      ? (this.nomineeType = nomineeTypeFromRoute === 'GENERAL' ? NomineeType.GENERAL : NomineeType.GRATUITY_FUND)
      : this.router.navigate(['/dashboard']);

    this.nominee = {
      ...new Nominee(),
      nomineeType: this.nomineeType,
    };
  }

  ngOnInit(): void {
    sessionStorage.setItem('selectedNomineeType', 'gf');
    this.loadEmployeeDetails();
  }

  loadGuardianInfo(gfNominee: INominee[]): void {
    this.guardianInfo = gfNominee.find(nominee => nominee.age! < 18)!;
    // if (this.guardianInfo === undefined) {
    //   this.guardianInfo = { ...new Nominee() };
    // }
  }

  loadRemainingSharePercentage(pfAccountId: number): void {
    this.pfNomineeService.getRemainingSharePercentage(pfAccountId).subscribe(res => {
      this.totalUsedPercentage = 100 - res;
    });
  }

  loadGfNominees(nominee: INominee): void {
    nominee.nomineeType = this.nomineeType;
    this.nomineeService.getNomineeListCommon(nominee).subscribe(res => {
      this.gfNominees = res.body!;
      this.loadGuardianInfo(this.gfNominees);
    });
  }

  loadEmployeeDetails(): void {
    this.nomineeService.getEmployeeDetailsForNomineeCommon().subscribe(res => {
      this.employeeDetails = res.body!;
      this.loadGfNominees(this.nominee);
    });
  }

  // showGfNomineeStatement(): void {
  //   this.pfNomineeService.getAllByPfAccountId(this.pfAccountIdFromRoute).subscribe(res => {
  //     this.pfNominees = res.body!;
  //     if (this.pfNominees.length === 0) {
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //     } else if (this.pfNominees.length === 1) {
  //       this.loadRemainingSharePercentage(this.pfNominees[0].pfAccountId!);
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //     } else if (this.pfNominees.length > 1) {
  //       this.loadRemainingSharePercentage(this.pfNominees[0].pfAccountId!);
  //     }
  //     this.loadEmployeeDetails(this.pfNominees[0].pin!);
  //     this.loadGuardianInfo();
  //   });
  // }

  // loadGuardianInfo(): void {
  //   if (this.pfNominees.length > 0) {
  //     for (let i = 0; i < this.pfNominees.length; i++) {
  //       const value = this.pfNominees[i];
  //       if (value.age! < 18) {
  //         this.guardianInfo = {
  //           guardianName: value.guardianName,
  //           guardianFatherOrSpouseName: value.guardianFatherOrSpouseName,
  //           guardianDateOfBirth: value.guardianDateOfBirth,
  //           guardianPresentAddress: value.guardianPresentAddress,
  //           guardianPermanentAddress: value.guardianPermanentAddress,
  //           guardianProofOfIdentityOfLegalGuardian: value.guardianProofOfIdentityOfLegalGuardian,
  //           guardianRelationshipWithNominee: value.guardianRelationshipWithNominee,
  //           guardianNidNumber: value.guardianNidNumber,
  //           guardianBrnNumber: value.guardianBrnNumber,
  //           guardianIdNumber: value.guardianIdNumber,
  //         };
  //         break;
  //       }
  //     }
  //   }
  // }

  print(): void {
    window.print();
  }

  delete(pfNominee: any): void {
    Swal.fire({
      text: 'Delete ?',
      showDenyButton: true,
      confirmButtonText: 'Yes',
      confirmButtonColor: '#55738f',
      denyButtonText: 'Cancel',
      denyButtonColor: '#f25d5a',
    }).then(result => {
      if (result.isConfirmed) {
        this.nomineeService.delete(pfNominee.id).subscribe(
          () => {
            this.deleteSuccess();
            // this.showGfNomineeStatement();
          },
          () => this.requestFailed()
        );
      }
    });
  }

  deleteSuccess(): void {
    swalOnDeleteSuccess();
    // this.showGfNomineeStatement();
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  getGfNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + id;
    return url;
  }

  getNomineeLetterHead(): string {
    return OrganizationFilesUrl.NOMINEE_LETTER_HEAD;
  }
}
