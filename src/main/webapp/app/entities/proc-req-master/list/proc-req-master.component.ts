import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import { swalConfirmationWithMessage } from '../../../shared/swal-common/swal-confirmation.common';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalSuccessWithMessage,
} from '../../../shared/swal-common/swal-common';
import { Router } from '@angular/router';

import { FormBuilder } from '@angular/forms';
import { IProcReqMaster } from '../proc-req-master.model';
import { IEmployee } from '../../employee/employee.model';
import { IDepartment } from '../../department/department.model';
import { DataUtils } from '../../../core/util/data-util.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EmployeeService } from '../../employee/service/employee.service';
import { DepartmentService } from '../../department/service/department.service';
import { ProcReqMasterService } from '../service/proc-req-master.service';

@Component({
  selector: 'jhi-proc-req-master',
  templateUrl: './proc-req-master.component.html',
})
export class ProcReqMasterComponent implements OnInit, OnDestroy {
  procReqMasters: IProcReqMaster[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  employeeList: IEmployee[] = [];
  departments: IDepartment[] = [];

  searchForm = this.fb.group({
    employeeId: [],
    departmentId: [],
    requisitionStatus: [],
    month: [],
    year: [],
  });

  constructor(
    protected procReqMasterService: ProcReqMasterService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private router: Router,
    private employeeService: EmployeeService,
    private fb: FormBuilder,
    protected departmentService: DepartmentService
  ) {
    this.procReqMasters = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.procReqMasterService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        // ...this.searchForm.value
        employeeId: this.searchForm.get(['employeeId'])!.value,
        departmentId: this.searchForm.get(['departmentId'])!.value,
        requisitionStatus: this.searchForm.get(['requisitionStatus'])!.value,
        year: this.searchForm.get(['year'])!.value,
        month: this.searchForm.get(['month'])!.value,
      })
      .subscribe((res: HttpResponse<IProcReqMaster[]>) => this.paginateProcReqMasters(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.procReqMasters = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProcReqMasters();

    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProcReqMaster): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInProcReqMasters(): void {
    this.eventSubscriber = this.eventManager.subscribe('procReqMasterListModification', () => this.reset());
  }

  delete(procReqMaster: IProcReqMaster): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.procReqMasterService.delete(procReqMaster.id!).subscribe((_: HttpResponse<any>) => {
          swalOnDeleteSuccess();
          this.eventManager.broadcast('procReqMasterListModification');
        });
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProcReqMasters(data: IProcReqMaster[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.procReqMasters.push(data[i]);
      }
    }
  }

  closePRF(procReqMasterId: number): void {
    swalConfirmationWithMessage('Close ?').then(response => {
      if (response.isConfirmed) {
        swalOnLoading('Processing');
        this.procReqMasterService.close(procReqMasterId).subscribe(() => {
          swalSuccessWithMessage('Closed!');
          this.reset();
        });
      }
    });
  }

  navigatePrintView(id: number): void {
    sessionStorage.setItem('prfPrintViewMidHeader', '/procurement-requisition-manage');
    this.router.navigate(['/procurement-requisition-manage', id, 'print-view']);
  }

  changeEmployee(employee: any): void {
    if (employee === undefined) {
      this.searchForm.patchValue({
        employeeId: null,
      });
    } else {
      this.searchForm.patchValue({
        employeeId: employee.id,
      });
    }
    this.reset();
  }

  selectYear(year: number): void {
    if (year === -1) {
      this.searchForm.patchValue({
        year: null,
      });
    } else {
      this.searchForm.patchValue({
        year,
      });
    }
    this.reset();
  }

  changeDepartment(event: any): void {
    if (event.target.value === 'null') {
      this.searchForm.patchValue({
        departmentId: null,
      });
    }
    this.reset();
  }

  changeRequisitionStatus(event: any): void {
    if (event.target.value === 'null') {
      this.searchForm.patchValue({
        requisitionStatus: null,
      });
    }
    this.reset();
  }

  selectMonth(month: number): void {
    // month number in string
    if (month === -1) {
      this.searchForm.patchValue({
        month: null,
      });
    } else {
      this.searchForm.patchValue({
        month,
      });
    }
    this.reset();
  }
}
