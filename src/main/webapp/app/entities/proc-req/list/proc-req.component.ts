import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess } from '../../../shared/swal-common/swal-common';
import { Router } from '@angular/router';
import { DefinedKeys } from '../../../shared/constants/defined-keys.constant';
import { FormControl } from '@angular/forms';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import { IDepartment } from '../../department/department.model';
import { ProcReqService } from '../service/proc-req.service';
import { ConfigService } from '../../config/service/config.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { DepartmentService } from '../../department/service/department.service';

@Component({
  selector: 'jhi-proc-req-master',
  templateUrl: './proc-req.component.html',
})
export class ProcReqComponent implements OnInit, OnDestroy {
  procReqMasters: IProcReqMaster[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  prfTeamContactNumber: string | undefined;
  departments: IDepartment[] = [];

  departmentFormControl = new FormControl(null);

  constructor(
    protected procReqUserService: ProcReqService,
    private configService: ConfigService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private router: Router,
    private departmentService: DepartmentService
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
    this.procReqUserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        departmentId: this.departmentFormControl.value,
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
    this.loadPRFTeamContactNumber();
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  loadPRFTeamContactNumber(): void {
    this.configService.findByKeyCommon(DefinedKeys.PRF_TEAM_CONTACT_NO).subscribe(response => {
      this.prfTeamContactNumber = response.body!.value;
    });
  }

  trackId(index: number, item: IProcReqMaster): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProcReqMasters(): void {
    this.eventSubscriber = this.eventManager.subscribe('procReqMasterListModification', () => this.reset());
  }

  delete(procReqMaster: IProcReqMaster): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.procReqUserService.delete(procReqMaster.id!).subscribe((_: HttpResponse<any>) => {
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

  navigatePrintView(id: number): void {
    sessionStorage.setItem('prfPrintViewMidHeader', '/procurement-requisition-user');
    this.router.navigate(['/procurement-requisition-user', id, 'print-view']);
  }
}
