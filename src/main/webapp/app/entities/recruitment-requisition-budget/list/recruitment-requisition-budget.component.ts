import { Component, OnInit, OnDestroy} from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';
import { RecruitmentRequisitionBudgetDeleteDialogComponent } from '../delete/recruitment-requisition-budget-delete-dialog.component';
import { IEmployeeMinimal } from '../../../shared/model/employee-minimal.model';
import { EventManager } from '../../../core/util/event-manager.service';
import { EmployeeService } from '../../employee/service/employee.service';

@Component({
  selector: 'jhi-recruitment-requisition-budget',
  templateUrl: './recruitment-requisition-budget.component.html',
})
export class RecruitmentRequisitionBudgetComponent implements OnInit, OnDestroy {
  recruitmentRequisitionBudgets?: IRecruitmentRequisitionBudget[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  employee?: IEmployeeMinimal;

  constructor(
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected employeeService: EmployeeService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.recruitmentRequisitionBudgetService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRecruitmentRequisitionBudget[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInRecruitmentRequisitionBudgets();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRecruitmentRequisitionBudget): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRecruitmentRequisitionBudgets(): void {
    this.eventSubscriber = this.eventManager.subscribe('recruitmentRequisitionBudgetListModification', () => this.loadPage());
  }

  delete(recruitmentRequisitionBudget: IRecruitmentRequisitionBudget): void {
    const modalRef = this.modalService.open(RecruitmentRequisitionBudgetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recruitmentRequisitionBudget = recruitmentRequisitionBudget;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IRecruitmentRequisitionBudget[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/recruitment-requisition-budget'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.recruitmentRequisitionBudgets = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
