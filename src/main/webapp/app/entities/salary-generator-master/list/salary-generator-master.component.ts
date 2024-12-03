import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';
import {ISalaryGeneratorMaster} from "../salary-generator-master.model";
import {SalaryGeneratorMasterService} from "../service/salary-generator-master.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {SalaryGeneratorMasterDeleteDialogComponent} from "../delete/salary-generator-master-delete-dialog.component";
import {numberToMonth} from "../../../shared/util/month-util";
import {
  swalOnChangeSalaryVisibilityConfirmation,
  swalOnGenerateSuccess,
  swalOnRequestError
} from "../../../shared/swal-common/swal-common";
import {DANGER_COLOR, INFO_COLOR, PRIMARY_COLOR} from "../../../config/color.code.constant";


@Component({
  selector: 'jhi-salary-generator-master',
  templateUrl: './salary-generator-master.component.html',
  styleUrls: ['salary-generator-master.component.scss'],
})
export class SalaryGeneratorMasterComponent implements OnInit, OnDestroy {
  salaryGeneratorMasters?: ISalaryGeneratorMaster[];
  eventSubscriber?: Subscription;
  selectedSalaryGeneratorMaster!: ISalaryGeneratorMaster;

  constructor(
    protected salaryGeneratorMasterService: SalaryGeneratorMasterService,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.salaryGeneratorMasterService
      .query()
      .subscribe((res: HttpResponse<ISalaryGeneratorMaster[]>) => (this.salaryGeneratorMasters = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSalaryGeneratorMasters();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISalaryGeneratorMaster): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSalaryGeneratorMasters(): void {
    this.eventSubscriber = this.eventManager.subscribe('salaryGeneratorMasterListModification', () => this.loadAll());
  }

  delete(salaryGeneratorMaster: ISalaryGeneratorMaster): void {
    const modalRef = this.modalService.open(SalaryGeneratorMasterDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.salaryGeneratorMaster = salaryGeneratorMaster;
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

  toDate(year: String, month: String): dayjs.Dayjs {
    return dayjs(new Date(Number(year), Number(month) - 1));
  }

  openSalaryReportModal(content: any, salaryGeneratorMaster: ISalaryGeneratorMaster): void {
    this.selectedSalaryGeneratorMaster = salaryGeneratorMaster;
    this.modalService.open(content, { size: 'md', centered: true }).result.then(
      result => {},
      reason => {}
    );
  }

  getMonthName(s: string): string {
    const monthNumber = Number.parseInt(s, 10);
    const monthName = numberToMonth(monthNumber);
    return monthName;
  }

  makeSalaryVisibleToEmployee(year: string, month: string): void {
    swalOnChangeSalaryVisibilityConfirmation('Are you sure you want to make the salary visible to all employee?').then(result => {
      if (result.isConfirmed) {
        this.salaryGeneratorMasterService.makeSalaryVisibleToEmployee(year, month).subscribe(res => {
          if (res.body === true) {
            swalOnGenerateSuccess(`Salary for month "${this.getMonthName(month.toString())}, ${year}" is now visible to all employee.`);
            this.loadAll();
          } else {
            swalOnRequestError();
          }
        });
      }
    });
  }

  makeSalaryHiddenFromEmployee(year: string, month: string): void {
    swalOnChangeSalaryVisibilityConfirmation('Are you sure you want to make the salary hidden from all employee?').then(result => {
      if (result.isConfirmed) {
        this.salaryGeneratorMasterService.makeSalaryHiddenFromEmployee(year, month).subscribe(res => {
          if (res.body === true) {
            swalOnGenerateSuccess(`Salary for month "${this.getMonthName(month.toString())}, ${year}" has been Hidden from all employee.`);
            this.loadAll();
          } else {
            swalOnRequestError();
          }
        });
      }
    });
  }

  onChangeVisibilityStatus(visibility: string, year: string, month: string): void {
    if (visibility === 'visible') {
      Swal.fire({
        showCancelButton: true,
        cancelButtonColor: DANGER_COLOR,
        confirmButtonText: 'Make Salary Hidden',
        confirmButtonColor: PRIMARY_COLOR,
      }).then(res => {
        if (res.isConfirmed) {
          this.makeSalaryHiddenFromEmployee(year, month);
        }
      });
    } else if (visibility === 'hidden') {
      Swal.fire({
        showCancelButton: true,
        cancelButtonColor: DANGER_COLOR,
        confirmButtonText: 'Make Salary Visible',
        confirmButtonColor: PRIMARY_COLOR,
      }).then(res => {
        if (res.isConfirmed) {
          this.makeSalaryVisibleToEmployee(year, month);
        }
      });
    } else if (visibility === 'partially_visible') {
      Swal.fire({
        showCancelButton: true,
        cancelButtonColor: DANGER_COLOR,
        showDenyButton: true,
        confirmButtonText: 'Make All Salary Visible',
        confirmButtonColor: PRIMARY_COLOR,
        denyButtonText: 'Make All Salary Hidden',
        denyButtonColor: INFO_COLOR,
      }).then(res => {
        if (res.isConfirmed) {
          this.makeSalaryVisibleToEmployee(year, month);
        } else if (res.isDenied) {
          this.makeSalaryHiddenFromEmployee(year, month);
        }
      });
    }
  }
}
