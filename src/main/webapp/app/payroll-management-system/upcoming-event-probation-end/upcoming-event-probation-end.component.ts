import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { Filter } from '../../common/employee-address-book/filter.model';
import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { UpcomingEventProbationEndService } from './upcoming-event-probation-end.service';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { ITEMS_PER_PAGE } from '../../config/pagination.constants';
import { IDesignation } from '../../shared/legacy/legacy-model/designation.model';
import { IDepartment } from '../../shared/legacy/legacy-model/department.model';
import { IUnit } from '../../shared/legacy/legacy-model/unit.model';
import { EventManager } from '../../core/util/event-manager.service';
import { DesignationService } from '../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../shared/legacy/legacy-service/department.service';
import { UnitService } from '../../shared/legacy/legacy-service/unit.service';
import { ASC, DESC } from '../../config/navigation.constants';
import {CustomValidator} from "../../validators/custom-validator";

@Component({
  selector: 'jhi-upcoming-event-probation-end',
  templateUrl: './upcoming-event-probation-end.component.html',
  styleUrls: ['upcoming-event-probation-end.component.scss'],
})
export class UpcomingEventProbationEndComponent implements OnInit, OnDestroy {
  employees?: IEmployee[];
  isLoading = false;
  eventSubscriber?: Subscription;

  predicate = 'id';
  ascending = true;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  ngbPaginationPage = 1;
  invalidYear!: boolean;

  designations!: IDesignation[];
  departments!: IDepartment[];
  units!: IUnit[];

  filters = new Filter();
  currentYear: number = new Date().getFullYear();
  years: number[];

  editForm = this.fb.group({
    month: [],
  });

  constructor(
    protected employeeService: EmployeeCommonService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    private designationService: DesignationService,
    private departmentService: DepartmentService,
    private upcomingEventProbationEndService: UpcomingEventProbationEndService,
    private unitService: UnitService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    private fb: FormBuilder
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];

    this.invalidYear = true;
    this.editForm.controls['month'].setValue(null);
    this.editForm.controls['month'].disable();
  }

  loadAll(): void {
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.upcomingEventProbationEndService
      .query(this.filters, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEmployee[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  ngOnInit(): void {
    this.loadPage(this.page);
    this.loadAll();
    this.handleNavigation();
    this.registerChangeInEmployees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmployee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployees(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeeListModification', () => this.loadPage());
  }

  isSearchTextInvalid = false;

  reloadDataAsSearchFilter(): void {

    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.filters.searchText)){
      this.isSearchTextInvalid = true
      return;
    }
    else this.isSearchTextInvalid = false

    const pageToLoad = 1;

    if (this.filters.departmentId === null) {
      this.filters.departmentId = 0;
    } else if (this.filters.destinationId === null) {
      this.filters.destinationId = 0;
    } else if (this.filters.unitId === null) {
      this.filters.unitId = 0;
    } else if (this.filters.year === null) {
      this.filters.year = 0;
    } else if (this.filters.month === null) {
      this.filters.month = '';
    }
    this.upcomingEventProbationEndService
      .query(this.filters, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEmployee[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );

    this.registerChangeInEmployees();
  }

  onDesignationSelect(): void {
    // alert(`${this.filters.departmentId}-${this.filters.destinationId}-${this.filters.unitId}-${this.filters.searchText}`);
    // this.filterEmployee();
    this.reloadDataAsSearchFilter();
  }

  onUnitSelect(): void {
    this.reloadDataAsSearchFilter();
  }

  onDepartmentSelect(): void {
    this.reloadDataAsSearchFilter();
  }

  onSearchTextChange(): void {
    this.reloadDataAsSearchFilter();
  }

  onChangeYear(event: any): void {
    this.filters.year = event.target.value;
    if (event.target.value === 'null' || event.target.value === undefined) {
      this.invalidYear = true;
      this.editForm.controls['month'].setValue(null);
      this.editForm.controls['month'].disable();
    } else {
      this.invalidYear = false;
      this.editForm.controls['month'].enable();
    }
    this.reloadDataAsSearchFilter();
  }

  onChangeMonth(event: any): void {
    if (event.target.value === 'null') {
      this.filters.month = undefined;
    } else {
      this.filters.month = event.target.value;
    }
    this.reloadDataAsSearchFilter();
  }

  protected onSuccess(data: IEmployee[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/upcoming-event-probation-end'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.employees = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }
}
