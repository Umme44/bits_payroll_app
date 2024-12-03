import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApprovalDTO } from 'app/shared/model/approval-dto.model';
import { LeaveApprovalLmService } from 'app/attendance-management-system/leave-approval-superordinate/leave-approval-superordinate.service';
import { FormControl } from '@angular/forms';
import { ILeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { EventManager } from '../../core/util/event-manager.service';
import { ParseLinks } from '../../core/util/parse-links.service';
import { ITEMS_PER_PAGE } from '../../config/pagination.constants';

@Component({
  selector: 'jhi-leave-approval-superordinate',
  templateUrl: './leave-approved-by-me.component.html',
  styleUrls: ['leave-approval-superordinate.component.scss'],
})
export class LeaveApprovedByMeComponent implements OnInit, OnDestroy {
  leaveApplications: ILeaveApplication[] = [];
  leaveApplicationsFiltered: ILeaveApplication[] = [];

  eventSubscriber?: Subscription;
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchTxt = new FormControl('');

  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected leaveApprovalLmService: LeaveApprovalLmService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.leaveApplications = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'sanctionedAt';
    this.ascending = false;
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLeaveApplications();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  loadAll(): void {
    this.leaveApprovalLmService
      .getAllApprovedByMe({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ILeaveApplication[]>) => {
        this.paginateILeaveApplication(res.body, res.headers);
      });
  }

  search(searchText: any): void {
    this.page = 0;
    this.leaveApplications = [];
    this.leaveApprovalLmService
      .getAllApprovedByMeFilter({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText,
      })
      .subscribe((res: HttpResponse<ILeaveApplication[]>) => {
        this.paginateILeaveApplication(res.body, res.headers);
      });
  }

  trackId(index: number, item: ILeaveApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.reset());
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  reset(): void {
    this.page = 0;
    this.leaveApplications = [];
    this.loadAll();
  }

  protected paginateILeaveApplication(data: ILeaveApplication[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.leaveApplications.push(data[i]);
      }
    }
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }
}
