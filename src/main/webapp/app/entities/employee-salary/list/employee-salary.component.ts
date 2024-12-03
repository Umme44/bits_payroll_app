import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, Subscription, timer } from 'rxjs';
import { timeout } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';

import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { IFraction } from 'app/shared/model/fraction.model';
import { DatePipe } from '@angular/common';
import { IAllowanceName } from 'app/shared/model/allowance-name.model';
import Swal from 'sweetalert2';
import {
  swalOnHoldOrUnholdSuccess,
  swalOnHoldOrUnholdConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnDeleteConfirmation,
  swalOnLoading,
  swalClose,
  swalOnChangeSalaryVisibilityConfirmation,
  swalOnGenerateSuccess,
} from 'app/shared/swal-common/swal-common';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION } from 'app/shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { numberToMonth } from 'app/shared/util/month-util';
import {IEmployeeSalary} from "../employee-salary.model";
import {ConfigService} from "../../config/service/config.service";
import {SalaryGeneratorMasterService} from "../../salary-generator-master/service/salary-generator-master.service";
import {EmployeeSalaryService} from "../service/employee-salary.service";
import {SalaryLockService} from "../../salary-generator-master/salary-lock/salary-lock-service";
import {EventManager} from "../../../core/util/event-manager.service";
import {EmployeeService} from "../../employee/service/employee.service";
import {SuccessMessageComponent} from "../../../shared/success-message/success-message.component";
import {TaxCalculationComponent} from "../tax-calculation/tax-calculation.component";
import {EmployeeCategory} from "../../enumerations/employee-category.model";

@Component({
  selector: 'jhi-employee-salary',
  templateUrl: './employee-salary.component.html',
  styleUrls: ['employee-salary.component.scss'],
})
export class EmployeeSalaryComponent implements OnInit, OnDestroy {
  employeeSalaries?: IEmployeeSalary[];
  eventSubscriber?: Subscription;
  isViewByYearMonth?: boolean;
  breakdownMap: Map<number, IFraction[]> = new Map();
  year = 0;
  month = 0;
  totalItems = 0;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  private ITEMS_PER_PAGE = 20;
  itemsPerPage = this.ITEMS_PER_PAGE;

  isSalaryLocked!: boolean;

  allowanceName: IAllowanceName | null = null;

  searchParamsForm = this.fb.group({
    searchText: [''],
  });
  isSingleEmployeeSalary!: boolean;

  constructor(
    protected configService: ConfigService,
    protected salaryGeneratorMasterService: SalaryGeneratorMasterService,
    protected employeeSalaryService: EmployeeSalaryService,
    protected salaryLockService: SalaryLockService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected employeeService: EmployeeService,
    protected datePipe: DatePipe,
    private router: Router,
    private fb: FormBuilder
  ) {}

  getBreakdown(id: number): IFraction[] {
    return this.breakdownMap.get(id)!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  loadBreakdown(): void {
    for (const employeeSalary of this.employeeSalaries!) {
      this.employeeSalaryService.findBreakDownForId(employeeSalary.id!).subscribe((fractions: HttpResponse<IFraction[]>) => {
        this.breakdownMap.set(employeeSalary.id!, fractions.body ? fractions.body : []);
      });
    }
  }

  ngOnInit(): void {
    this.isSingleEmployeeSalary = this.activatedRoute.snapshot.data['isSingleEmployeeSalary'];

    this.configService.getAllowanceName().subscribe(res => {
      this.allowanceName = res.body;
    });

    this.handleNavigation();
    this.registerChangeInEmployeeSalaries();
    this.checkThisMonthSalaryIsLocked();
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

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    const year = this.activatedRoute.snapshot.params['year'];
    this.year = year;
    const month = this.activatedRoute.snapshot.params['month'];
    this.month = month;

    const options = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      searchText: this.searchParamsForm.get('searchText')!.value,
    };

    swalOnLoading('Loading Salaries');

    if (year !== undefined && month !== undefined && this.isSingleEmployeeSalary === false) {
      /* for one month all employee salary details */
      this.isViewByYearMonth = true;
      this.employeeSalaryService.queryWithParams(year, month, options).subscribe((res: HttpResponse<IEmployeeSalary[]>) => {
        swalClose();
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        this.loadBreakdown();
      });
      () => this.onError();
    } else if (year !== undefined && month !== undefined && this.isSingleEmployeeSalary === true) {
      /* for single employee salary details */
      if (this.isSingleEmployeeSalary === true) {
        const employeeId = this.activatedRoute.snapshot.params['employeeId'];
        this.isViewByYearMonth = true;
        this.employeeSalaryService.queryWithEmployeeId(employeeId, year, month).subscribe((res: HttpResponse<IEmployeeSalary[]>) => {
          swalClose();
          this.searchParamsForm.get('searchText')!.setValue(res.body![0].pin + '-' + res.body![0].employeeName);
          this.searchParamsForm.get('searchText')!.disable();
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          this.loadBreakdown();
        });
        () => this.onError();
      }
    } else {
      /* for all generated employee salary details */
      this.isViewByYearMonth = false;
      this.employeeSalaryService.query(options).subscribe((res: HttpResponse<IEmployeeSalary[]>) => {
        swalClose();
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        this.loadBreakdown();
      });
      () => this.onError();
    }
  }

  trackId(index: number, item: IEmployeeSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployeeSalaries(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeeSalaryListModification', () => this.loadPage());
  }

  delete(employeeSalary: IEmployeeSalary): void {
    /* const modalRef = this.modalService.open(EmployeeSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
     modalRef.componentInstance.employeeSalary = employeeSalary;*/
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryService.delete(employeeSalary.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.searchParamsForm.get('searchText')!.setValue('');

            setTimeout(() => {
              this.loadPage();
            }, 2000);
          },
          error => {
            swalOnRequestErrorWithBackEndErrorTitle(error.error.title);
          }
        );
      }
    });
  }

  taxCalcModal(employeeSalary: IEmployeeSalary): void {
    if (employeeSalary.employeeCategory === 'INTERN') {
      return;
    }

    const modalRef = this.modalService.open(TaxCalculationComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.text = employeeSalary.taxCalculationSnapshot;
  }

  navigateToSalaryPayslip(employeeSalary: IEmployeeSalary): void {
    if (employeeSalary.employeeCategory === 'INTERN') {
      return;
    }
    this.router.navigate(['/', employeeSalary.id, 'monthly-salary-pay-slip']);
  }

  finalize(): void {
    this.employeeSalaryService.finalize(this.year, this.month).subscribe((resp: HttpResponse<boolean>) => {
      if (resp.body === true) {
        // alert('Successfully finalized salary of all active employees');
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Successfully finalized salary of all active employees';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.loadPage();
          }
        });
      } else {
        // alert('failed : contract system administrator');
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'failed : contract system administrator';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            const x = 'empty block removal';
          }
        });
      }
    });
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: any, params: ParamMap) => {
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

  protected onSuccess(data: IEmployeeSalary[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate([`/employee-salary/${this.year}/${this.month}`], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.employeeSalaries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  export(year: String, month: String, type: String): void {
    // window.open('/api/payroll-mgt/export/EmployeeSalary/' + year + '/' + month, '_blank');
    let fileName = 'salary_of_' + month + '_' + year + '_' + type + '.xlsx';
    if (type === 'short-summary') fileName = type + '_report_' + month + '_' + year + '.csv';
    if (type === 'summary') fileName = type + '_report_' + month + '_' + year + '.csv';
    this.salaryGeneratorMasterService.exportSalaryXlsx(year, month, type).subscribe(x => {
      // It is necessary to create a new blob object with mime-type explicitly set
      // otherwise only Chrome works like it should
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }

      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }

  salaryLockAlert(): void {
    if (this.isSalaryLocked) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'This Month Salary is Locked!',
      });
    }
  }

  userFriendlyEmployeeCategory(employeeCategory: EmployeeCategory): string {
    if (employeeCategory === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) return 'Regular Confirmed';
    else if (employeeCategory === EmployeeCategory.CONTRACTUAL_EMPLOYEE) return 'Contractual';
    else if (employeeCategory === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) return 'Regular Provisional';
    else if (employeeCategory === EmployeeCategory.INTERN) return 'Intern';
    else return 'Unknown';
  }

  getEmployeeInfoAsTooltip(fullName: any, pin: any): string {
    return fullName + '-' + pin;
  }

  holdEmployeeSalary(employeeSalary: IEmployeeSalary): void {
    swalOnHoldOrUnholdConfirmation('Hold Salary?').then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryService.holdEmployeeSalary(employeeSalary.id!).subscribe(res => {
          if (res.body) {
            swalOnHoldOrUnholdSuccess();
            this.loadPage();
          } else {
            swalOnRequestError();
            this.loadPage();
          }
        });
      }
    });
  }

  unHoldEmployeeSalary(employeeSalary: IEmployeeSalary): void {
    swalOnHoldOrUnholdConfirmation('Unhold Salary?').then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryService.unHoldEmployeeSalary(employeeSalary.id!).subscribe(res => {
          if (res.body) {
            swalOnHoldOrUnholdSuccess();
            this.loadPage();
          } else {
            swalOnRequestError();
            this.loadPage();
          }
        });
      }
    });
  }

  makeSalaryVisibleToEmployee(id: number, year: number, month: string): void {
    swalOnChangeSalaryVisibilityConfirmation('Are you sure you want to make this salary visible to the employee?').then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryService.makeSalaryVisibleToEmployee(id, year, month).subscribe(res => {
          if (res.body === true) {
            swalOnGenerateSuccess(`Salary for month "${this.getMonthName(month.toString())}, ${year}" is now visible to the employee.`);
            this.loadPage(0);
          } else {
            swalOnRequestError();
          }
        });
      }
    });
  }

  makeSalaryHiddenFromEmployee(id: number, year: number, month: string): void {
    swalOnChangeSalaryVisibilityConfirmation('Are you sure you want to make this salary hidden from the employee?').then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryService.makeSalaryHiddenFromEmployee(id, year, month).subscribe(res => {
          if (res.body === true) {
            swalOnGenerateSuccess(`Salary for month "${this.getMonthName(month.toString())}, ${year}" has been Hidden from the employee.`);
            this.loadPage(0);
          } else {
            swalOnRequestError();
          }
        });
      }
    });
  }

  getMonthName(s: string): string {
    const monthNumber = Number.parseInt(s, 10);
    const monthName = numberToMonth(monthNumber);
    return monthName;
  }
}
