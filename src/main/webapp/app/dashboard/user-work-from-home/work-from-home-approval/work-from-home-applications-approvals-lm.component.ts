import { Component, HostListener, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IWorkFromHomeApplication } from '../../../shared/legacy/legacy-model/work-from-home-application.model';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { WorkFromHomeApplicationsApprovalsService } from './work-from-home-applications-approval.service';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_DISABLED,
  SWAL_DISABLED_CONFIRMATION,
  SWAL_ENABLED,
  SWAL_ENABLED_CONFIRMATION,
  SWAL_REJECTED_ICON,
} from '../../../shared/swal-common/swal.properties.constant';
import {swalOnRequestError, swalPatternError} from '../../../shared/swal-common/swal-common';
import { UserWorkFromApplicationDetailModalComponent } from '../user-work-from-home-application/modal/user-work-from-application-detail-modal.component';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-work-from-home-applications-approvals-lm',
  templateUrl: './work-from-home-applications-approvals-lm.component.html',
})
export class WorkFromHomeApplicationsApprovalsLMComponent implements OnInit {
  pageType!: String;
  employeeList: IEmployee[] = [];
  workFromHomeApplicationList: IWorkFromHomeApplication[] = [];

  itemsPerPage = ITEMS_PER_PAGE;
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchText!: string;
  predicate!: string;
  predicate2!: string;
  ascending!: boolean;
  approvalType = 'pending';
  isSelectedByToggle = false;
  links: any;
  page: number;

  selectStatusType!: string;

  statusType: string[] = ['Active', 'Inactive'];
  isEnabling = false;

  searchForm = this.fb.group({
    searchTxt: [''],
    selectStatusType: [''],
  });

  constructor(
    protected workFromHomeApplicationsApprovalsService: WorkFromHomeApplicationsApprovalsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected modalService: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.predicate2 = 'startDate';
    this.ascending = true;
    this.approvalType = 'pending';
    this.searchText = '';
    this.pageType = 'lm';
  }

  ngOnInit(): void {
    if (this.pageType === 'lm') {
      this.loadPage(this.page);
    } else {
      this.approvalType = 'pending';
    }
    this.approvalDTO.listOfIds = [];
  }

  loadPage(page: number): void {
    this.page = page;
    if (this.approvalType === 'pending') {
      this.loadAllPendingLMList();
    } else {
      this.loadAllSubordinateLMList();
    }
  }
  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  sortByStartDate(): string[] {
    const result = [this.predicate2 + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate2 !== 'startDate') {
      result.push('startDate');
    }
    return result;
  }

  loadAllSubordinateLMList(): void {
    this.workFromHomeApplicationsApprovalsService
      .getAllSubordinateEmployeeListLM({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body) {
          this.paginateEmployee(res.body, res.headers);
          this.workFromHomeApplicationList.filter(s => (s.isChecked = false));
        }
      });
  }

  // get all  pending subordinate employee List for work-from-home admin approval page(LM)
  loadAllPendingLMList(): void {
    this.workFromHomeApplicationsApprovalsService
      .getPendingSubordinateEmployeeListLM({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sortByStartDate(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body) {
          this.paginateEmployee(res.body, res.headers);
          this.workFromHomeApplicationList.filter(s => (s.isChecked = false));
        }
      }, (err)=> {
        swalPatternError()
      });
  }

  protected paginateEmployee(data: IWorkFromHomeApplication[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    // this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.workFromHomeApplicationList.push(data[i]);
      }
    }
  }

  setSearchText(event: any): void {
    this.searchText = event.toString();
    this.filterByApprovalType(event);
  }

  approveSelected(): void {
    Swal.fire({
      text: SWAL_ENABLED_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isEnabling = true;
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.workFromHomeApplicationsApprovalsService.enableSelectedLM(this.approvalDTO));
        }
      } /*else {
        if (this.isSelectedByToggle) {
          this.approvalDTO.listOfIds = [];
        }
        this.isSelectedByToggle = false;
      }*/
    });
  }

  reset(): void {
    this.page = 0;
    this.workFromHomeApplicationList = [];
    this.loadAllSubordinateLMList();
  }
  rejectSelected(): void {
    Swal.fire({
      text: SWAL_DISABLED_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isEnabling = false;
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.workFromHomeApplicationsApprovalsService.disableSelectedLM(this.approvalDTO));
        }
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<boolean>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  private onSaveSuccess(): void {
    if (this.isEnabling) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: SWAL_ENABLED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_DISABLED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.clearAllChecks();
    this.loadAllAfterApproval();
  }

  loadAllAfterApproval(): void {
    if (this.pageType === 'lm') {
      if (this.approvalType === 'pending') {
        this.workFromHomeApplicationList = [];
        this.loadAllPendingLMList();
      } else {
        this.workFromHomeApplicationList = [];
        this.loadAllSubordinateLMList();
      }
    }
  }

  private onSaveError(): void {
    swalOnRequestError();
  }

  getProfilePicture(pin: String): String {
    return SERVER_API_URL + '/files/get-employees-image/' + pin;
  }
  changeStatus(employeeId: any, isEnabling: boolean): void {
    if (this.approvalType !== 'all') {
      this.isSelectedByToggle = true;
      this.approvalDTO.listOfIds = [employeeId];
      if (isEnabling) {
        this.rejectSelected();
      } else {
        this.approveSelected();
      }
    }
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.approvalDTO.listOfIds = [];
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.workFromHomeApplicationList !== undefined) {
      this.workFromHomeApplicationList = this.workFromHomeApplicationList.map(d => {
        if (d.id === id) {
          d.isChecked = isChecked;
          this.allSelector = false;
          return d;
        }
        if (id === -1) {
          d.isChecked = this.allSelector;
          return d;
        }
        return d;
      });
    }
    // clear previous set
    this.selectedIdSet.clear();
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.workFromHomeApplicationList!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.workFromHomeApplicationList![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.workFromHomeApplicationList![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  trackId(index: number, item: IWorkFromHomeApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  getEmployeeNameList(employees: IWorkFromHomeApplication[]): string[] {
    return employees.map(x => {
      return x.fullName!;
    });
  }
  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.employeeList.forEach(emp => {
      emp.isChecked = false;
    });
  }

  filterByApprovalType(event: any): void {
    if(this.showInvalidSearchTextError(this.searchText)) {
      this.clearAllChecks();
      if (event.toString() === 'all' || event.toString() === 'pending') {
        this.approvalType = event.toString();
      }
      if (this.approvalType === 'pending') {
        this.page = 0;
        this.workFromHomeApplicationList = [];
        this.loadAllPendingLMList();
      } else {
        this.page = 0;
        this.workFromHomeApplicationList = [];
        this.loadAllSubordinateLMList();
      }
    }
    else this.isSearchTextInvalid = true;
  }

  isSearchTextInvalid = false;
  showInvalidSearchTextError(searchText: string): boolean{
    this.isSearchTextInvalid = !CustomValidator.NATURAL_TEXT_PATTERN.test(searchText)
    return !this.isSearchTextInvalid
  }

  openWorkFromApplicationDetails(workFromHomeApplication: IWorkFromHomeApplication): void {
    const modalRef = this.modalService.open(UserWorkFromApplicationDetailModalComponent, { size: 'lg', backdrop: true, keyboard: false });
    modalRef.componentInstance.workFromHomeApplication = workFromHomeApplication;
  }
}
