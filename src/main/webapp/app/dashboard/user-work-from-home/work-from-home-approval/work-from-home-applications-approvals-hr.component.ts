import { Component, HostListener, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { WorkFromHomeApplicationsApprovalsService } from './work-from-home-applications-approval.service';
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

import { swalOnRequestError } from '../../../shared/swal-common/swal-common';

import { UserWorkFromApplicationDetailModalComponent } from '../user-work-from-home-application/modal/user-work-from-application-detail-modal.component';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { IWorkFromHomeApplication } from '../../../shared/legacy/legacy-model/work-from-home-application.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-work-from-home-applications-approvals-hr',
  templateUrl: './work-from-home-applications-approvals-hr.component.html',
})
export class WorkFromHomeApplicationsApprovalsHRComponent implements OnInit {
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
  approvalType = 'all';
  isSelectedByToggle = false;
  links: any;
  page: number;

  selectStatusType!: string;
  activeEmployees!: number;
  inActiveEmployees!: number;
  totalActiveEmployees!: number;

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
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.predicate2 = 'startDate';
    this.ascending = true;
    this.approvalType = 'all';
    this.searchText = '';
    this.pageType = 'hr';
    this.searchForm.get('selectStatusType')!.setValue(null);
  }

  ngOnInit(): void {
    if (this.pageType === 'hr') {
      this.loadAllEmployeeListForHr();
      this.workFromHomeApplicationsApprovalsService.getTotalInActiveEmployeeListCountForHr().subscribe(res => {
        if (res.body!) {
          this.inActiveEmployees = res.body!;
        }
      });

      this.workFromHomeApplicationsApprovalsService.getTotalActiveEmployeeListCountForHr().subscribe(res => {
        if (res.body!) {
          this.activeEmployees = res.body!;
        }
      });
    } else {
      /* this.workFromHomeApplicationsApprovalsService.getEmployeeListHR().subscribe((res: HttpResponse<IEmployee[]>) => {
        if (res.body) {
          this.employeeListFiltered = this.employeeList = res.body;
        }
      });*/
    }

    //this.loadAll();
    //this.registerChangeInManualAttendanceEntries();
    this.approvalDTO.listOfIds = [];
  }

  loadPage(page: number): void {
    this.page = page;
    if (this.approvalType === 'pending') {
      this.loadAllPendingHRList();
    } else {
      const value = this.searchForm.get('selectStatusType')!.value;
      if (value === 'Active') {
        this.loadAllActiveEmployeeListForHr();
      } else if (value === 'Inactive') {
        this.loadAllInActiveEmployeeListForHr();
      } else {
        this.loadAllEmployeeListForHr();
      }
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

  // get all  pending subordinate employee List for work-from-home admin approval page(HR)
  loadAllPendingHRList(): void {
    this.workFromHomeApplicationsApprovalsService
      .getPendingEmployeeListHR({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sortByStartDate(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body!) {
          this.paginateEmployee(res.body, res.headers);
          this.workFromHomeApplicationList.filter(s => (s.isChecked = false));
        }
      });
  }

  // get all  employee List for work-from-home admin approval page(HR)
  loadAllEmployeeListForHr(): void {
    this.workFromHomeApplicationsApprovalsService
      .getEmployeeListForHr({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
        onlineAttendance: '',
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body!) {
          this.paginateEmployee(res.body, res.headers);
        }
      });
  }

  protected paginateEmployee(data: IWorkFromHomeApplication[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.workFromHomeApplicationList.push(data[i]);
      }
    }
  }

  // get all employee List for work-from-home admin active online attendance(HR)
  loadAllInActiveEmployeeListForHr(): void {
    this.workFromHomeApplicationsApprovalsService
      .getInActiveEmployeeListForHr({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body!) {
          this.paginateEmployee(res.body, res.headers);
        }
      });
  }

  // get all employee List for work-from-home admin inactive online attendance(HR)
  loadAllActiveEmployeeListForHr(): void {
    this.workFromHomeApplicationsApprovalsService
      .getActiveEmployeeListForHr({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IWorkFromHomeApplication[]>) => {
        if (res.body!) {
          this.paginateEmployee(res.body, res.headers);
        }
      });
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
        if (this.pageType === 'hr') {
          this.subscribeToSaveResponse(this.workFromHomeApplicationsApprovalsService.enableSelectedHR(this.approvalDTO));
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
    this.loadAllEmployeeListForHr();
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
        if (this.pageType === 'hr') {
          this.subscribeToSaveResponse(this.workFromHomeApplicationsApprovalsService.disableSelectedHR(this.approvalDTO));
        }
      } /* else {
        if (this.isSelectedByToggle) {
          this.approvalDTO.listOfIds = [];
        }
        this.isSelectedByToggle = false;
      }*/
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
    if (this.pageType === 'hr') {
      if (this.approvalType === 'pending') {
        this.workFromHomeApplicationList = [];
        this.loadAllPendingHRList();
      } else {
        this.workFromHomeApplicationList = [];
        this.loadAllEmployeeListForHr();
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
  @HostListener('document:keyup.escape', ['$event'])
  onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.employeeList.forEach(emp => {
      emp.isChecked = false;
    });
  }

  filterByApprovalType(event: any): void {
    this.clearAllChecks();
    if (event.toString() === 'all' || event.toString() === 'pending') {
      this.approvalType = event.toString();
    }

    if (this.approvalType === 'pending') {
      this.workFromHomeApplicationList = [];
      this.loadAllPendingHRList();
    } else {
      const value = this.searchForm.get('selectStatusType')!.value;
      if (value === 'Active') {
        this.page = 0;
        this.workFromHomeApplicationList = [];
        this.loadAllActiveEmployeeListForHr();
      } else if (value === 'Inactive') {
        this.page = 0;
        this.workFromHomeApplicationList = [];
        this.loadAllInActiveEmployeeListForHr();
      } else {
        this.page = 0;
        this.workFromHomeApplicationList = [];
        this.loadAllEmployeeListForHr();
      }
    }
  }

  openWorkFromApplicationDetails(workFromHomeApplication: IWorkFromHomeApplication): void {
    const modalRef = this.modalService.open(UserWorkFromApplicationDetailModalComponent, { size: 'lg', backdrop: true, keyboard: false });
    modalRef.componentInstance.workFromHomeApplication = workFromHomeApplication;
  }
}
