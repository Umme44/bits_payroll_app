import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';
import dayjs from 'dayjs/esm';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ISalaryDeduction } from '../salary-deduction.model';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import { IDeductionType } from '../../deduction-type/deduction-type.model';
import { EmployeeCustomService } from '../../employee-custom/service/employee-custom.service';
import { SalaryLockService } from '../../salary-generator-master/salary-lock/salary-lock-service';
import { SalaryDeductionService } from '../service/salary-deduction.service';
import { DeductionTypeService } from '../../deduction-type/service/deduction-type.service';
import { SalaryDeductionDeleteDialogComponent } from '../delete/salary-deduction-delete-dialog.component';

@Component({
  selector: 'jhi-salary-deduction',
  templateUrl: './salary-deduction.component.html',
})
export class SalaryDeductionComponent implements OnInit {
  salaryDeductions?: ISalaryDeduction[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isViewByYearMonth = false;
  year = 0;
  month = 0;
  employees!: IEmployee[];
  deductionTypes!: IDeductionType[];

  isSalaryLocked!: boolean;

  currentYear: number = new Date().getFullYear();
  years: number[];
  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];

  searchParamsForm = this.fb.group({
    month: [0],
    year: [0],
    searchText: [''],
  });

  constructor(
    protected salaryDeductionService: SalaryDeductionService,
    protected deductionTypeService: DeductionTypeService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected salaryLockService: SalaryLockService,
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
    ];
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    if (this.year !== 0 && this.month !== 0) {
      this.isViewByYearMonth = true;
      this.salaryDeductionService
        .queryWithParams(this.year, this.month, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchParamsForm.get('searchText')!.value,
        })
        .subscribe(
          (res: HttpResponse<ISalaryDeduction[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    } else {
      this.isViewByYearMonth = false;
      this.salaryDeductionService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchParamsForm.get('searchText')!.value,
          month: this.searchParamsForm.get('month')!.value,
          year: this.searchParamsForm.get('year')!.value,
        })
        .subscribe(
          (res: HttpResponse<ISalaryDeduction[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    }
  }

  ngOnInit(): void {
    this.year = this.activatedRoute.snapshot.params['year'] === undefined ? 0 : this.activatedRoute.snapshot.params['year'];
    this.month = this.activatedRoute.snapshot.params['month'] === undefined ? 0 : this.activatedRoute.snapshot.params['month'];

    this.handleNavigation();
    this.checkThisMonthSalaryIsLocked();
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
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    this.deductionTypeService.query().subscribe((res: HttpResponse<IDeductionType[]>) => (this.deductionTypes = res.body || []));
  }

  trackId(index: number, item: ISalaryDeduction): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(salaryDeduction: ISalaryDeduction): void {
    const modalRef = this.modalService.open(SalaryDeductionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salaryDeduction = salaryDeduction;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ISalaryDeduction[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      if (this.isViewByYearMonth) {
        this.router.navigate([`/salary-deduction/${this.year}/${this.month}`], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchParamsForm.get('searchText')!.value,
          },
        });
      } else {
        this.router.navigate(['/salary-deduction'], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchParamsForm.get('searchText')!.value,
          },
        });
      }
    }
    this.salaryDeductions = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  getEmployeeById(id: number): IEmployee {
    return this.employees.find(x => x.id === id)!;
  }

  toDate(year: number, month: number): dayjs.Dayjs {
    // return dayjs(Number(year), Number(month) - 1);
    return dayjs(`${year}-${month}-01`);
  }

  checkThisMonthSalaryIsLocked(): void {
    //check selected month salary is locked
    if (this.isViewByYearMonth) {
      this.salaryLockService.isSalaryLocked(this.month.toString(), this.year.toString()).subscribe(isLocked => {
        if (isLocked.body && isLocked.body === true) {
          this.isSalaryLocked = true;
        }
      });
    }
  }
}
