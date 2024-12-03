import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IEmployeeResignation} from "../employee-resignation.model";
import { IEmployee } from 'app/entities/employee/employee.model';
import {EmployeeResignationService} from "../service/employee-resignation.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {EmployeeResignationDeleteDialogComponent} from "../delete/employee-resignation-delete-dialog.component";
import dayjs from 'dayjs/esm';
import {DATE_FORMAT} from "../../../config/input.constants";
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-employee-resignation',
  templateUrl: './employee-resignation.component.html',
})
export class EmployeeResignationComponent implements OnInit, OnDestroy {
  separationType = 'resignation';
  employeeResignations?: IEmployeeResignation[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  employees: IEmployee[] = [];
  isInvalid = false;

  searchParamsForm = this.fb.group({
    startDate: [''],
    endDate: [''],
    searchText: ['', [CustomValidator.naturalTextValidator()]],
  });

  constructor(
    protected employeeResignationService: EmployeeResignationService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.employeeResignationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
      })
      .subscribe(
        (res: HttpResponse<IEmployeeResignation[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInEmployeeResignations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmployeeResignation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployeeResignations(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeeResignationListModification', () => this.loadPage());
  }

  delete(employeeResignation: IEmployeeResignation): void {
    const modalRef = this.modalService.open(EmployeeResignationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employeeResignation = employeeResignation;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  handleSeparationTypeChange(event: any): void {
    this.separationType = event.target.value;
  }

  protected onSuccess(data: IEmployeeResignation[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/employee-resignation'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          searchText: this.searchParamsForm.get('searchText')!.value,
        },
      });
    }
    this.employeeResignations = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  checkDate(): void {
    const startDate = this.searchParamsForm.get(['startDate'])!.value.format('YYYY-MM-DD');
    const endDate = this.searchParamsForm.get(['endDate'])!.value.format('YYYY-MM-DD');

    if (startDate !== undefined && endDate !== undefined && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  searchOnList(): void {
    const pageToLoad = 1;
    const startDate1 = this.searchParamsForm.get('startDate')!.value
      ? dayjs(this.searchParamsForm.get('startDate')!.value).format(DATE_FORMAT)
      : '';
    const endDate1: string = this.searchParamsForm.get('endDate')!.value
      ? dayjs(this.searchParamsForm.get('endDate')!.value).format(DATE_FORMAT)
      : '';

    this.employeeResignationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
        startDate: startDate1,
        endDate: endDate1,
      })
      .subscribe(
        (res: HttpResponse<IEmployeeResignation[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
  }
}
