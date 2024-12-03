import { Component, OnInit } from '@angular/core';
import { HoldSalariesApprovalService } from './hold-salaries-approval-service';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TITLE,
  SWAL_SURE,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { HoldSalaryDisbursement, IHoldSalaryDisbursement } from '../../../shared/legacy/legacy-model/hold-salary-disbursement.model';
import { textNormalize } from '../../../shared/common-util-methods/common-util-methods';
import { IEmployeeSalary } from '../../../shared/legacy/legacy-model/employee-salary.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { HoldSalaryDisbursementService } from '../../../shared/legacy/legacy-service/hold-salary-disbursement.service';

@Component({
  selector: 'jhi-salary-hold-list',
  templateUrl: './hold-salaries-approval-component.html',
  styleUrls: ['./hold-salaries-approval-component.scss'],
})
export class HoldSalariesApprovalComponent implements OnInit {
  holdSalaries!: IEmployeeSalary[];
  selectedEmployeeSalary!: IEmployeeSalary;
  searchText = '';

  // date = new FormControl(null);

  editForm = this.fb.group({
    date: [null, [Validators.required]],
  });

  constructor(
    protected salaryHoldService: HoldSalariesApprovalService,
    protected holdSalaryDisbursementService: HoldSalaryDisbursementService,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.salaryHoldService.getHoldSalaries({ searchText: this.searchText }).subscribe(res => {
      this.holdSalaries = res.body!;
    });
  }

  disburseSalaryModal(content: any, salary: IEmployeeSalary): void {
    this.selectedEmployeeSalary = salary;
    this.modalService.open(content).result.then(
      result => {
        this.swalConfirmation();
      },
      reason => {}
    );
  }

  swalConfirmation(): void {
    Swal.fire({
      text: SWAL_SURE,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.subscribeToSaveResponse(this.holdSalaryDisbursementService.create(this.createFromForm()));
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoldSalaryDisbursement>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    Swal.fire({
      icon: SWAL_APPROVED_ICON,
      text: 'Disbursed',
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.editForm.get(['date'])!.reset();
    this.loadAll();
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      title: SWAL_RESPONSE_ERROR_TITLE,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
    this.loadAll();
  }

  protected createFromForm(): IHoldSalaryDisbursement {
    return {
      ...new HoldSalaryDisbursement(),
      employeeSalaryId: this.selectedEmployeeSalary.id,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_FORMAT) : undefined,
    };
  }

  search(searchText: any): void {
    this.searchText = searchText;
    this.loadAll();
  }

  trackId(index: number, item: IEmployeeSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  monthNameNormalize(month: any): string {
    if (month) {
      return textNormalize(month);
    }
    return '';
  }
}
