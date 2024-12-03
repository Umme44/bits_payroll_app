import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LeaveSummaryEndUserViewService } from './leave-summary-end-user-view.component.service';
import { EventManager } from 'app/core/util/event-manager.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';

@Component({
  selector: 'jhi-leave-summary-end-user-view',
  templateUrl: './leave-summary-end-user-view.component.html',
  styleUrls: ['leave-summary-end-user-view.component.scss'],
})
export class LeaveSummaryEndUserViewComponent implements OnInit {
  leaveBalanceEndUserView?: ILeaveBalanceEndUserView[];
  eventSubscriber?: Subscription;
  currentYear: number = new Date().getFullYear();
  years: number[];

  editForm = this.fb.group({
    year: [],
  });

  constructor(
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
      this.currentYear - 6,
      this.currentYear - 7,
      this.currentYear - 8,
      this.currentYear - 9,
      this.currentYear - 10,
    ];
  }

  loadAll(): void {
    this.leaveSummaryEndUserViewService
      .query()
      .subscribe((res: HttpResponse<ILeaveBalanceEndUserView[]>) => (this.leaveBalanceEndUserView = res.body || []));
  }

  ngOnInit(): void {
    this.registerChangeInLeaveBalances();
    this.editForm.patchValue({
      year: this.years[0],
    });
    this.loadDataByYear();
  }

  loadDataByYear(): void {
    this.leaveSummaryEndUserViewService
      .loadByYear(this.editForm.get(['year'])!.value)
      .subscribe((resp: HttpResponse<ILeaveBalanceEndUserView[]>) => {
        this.leaveBalanceEndUserView = resp.body || [];
      });
    this.registerChangeInLeaveBalances();
  }

  onYearSelect(): void {
    this.loadDataByYear();
  }

  registerChangeInLeaveBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveBalanceListModification', () => this.loadAll());
  }

  trackId(index: number, item: ILeaveBalanceEndUserView): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }
}
