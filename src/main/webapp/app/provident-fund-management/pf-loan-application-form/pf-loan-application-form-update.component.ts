import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { PfLoanApplicationFormService } from './pf-loan-application-form.service';
import { Status } from '../../shared/model/enumerations/status.model';
import dayjs from 'dayjs/esm';
import { IPfLoanApplication, PfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { IPfAccount } from '../../shared/legacy/legacy-model/pf-account.model';
import { PfAccountService } from '../../shared/legacy/legacy-service/pf-account.service';

type SelectableEntity = IEmployee | IPfAccount;

@Component({
  selector: 'jhi-pf-loan-application-form',
  templateUrl: './pf-loan-application-form-update.component.html',
  styleUrls: ['./pf-loan-application-form.scss'],
})
export class PfLoanApplicationFormUpdateComponent implements OnInit {
  isSaving = false;
  pfAccounts: IPfAccount[] = [];
  pfLoanEligibleAmount!: number;
  recommendationDateDp: any;
  approvalDateDp: any;
  rejectionDateDp: any;
  disbursementDateDp: any;

  startRange?: dayjs.Dayjs;
  endRange?: dayjs.Dayjs;
  durationWithoutCalc: Number = 0;
  isDateInvalid = false;
  isConflict = false;
  isDaysEqualZero = false;

  editForm = this.fb.group({
    id: [],
    installmentAmount: [null, [Validators.required, Validators.min(1)]],
    noOfInstallment: [null, [Validators.required, Validators.min(1), Validators.max(24)]],
    disbursementDate: [null, [Validators.required]],
    disbursementAmount: [],
    status: [],
    pfAccountId: [],
  });

  constructor(
    protected pfLoanApplicationService: PfLoanApplicationFormService,
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.pfLoanApplicationService.getEmployeePfLoanEligibleAmount().subscribe(response => {
      this.pfLoanEligibleAmount = Number(response);
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanApplication }) => {
      // this.updateForm(pfLoanApplication);
      // this.pfAccountService.getCurrentUserPfAccounts().subscribe((res: HttpResponse<IPfAccount[]>) => (this.pfAccounts = res.body || []));
    });
  }

  updateForm(pfLoanApplication: IPfLoanApplication): void {
    this.editForm.patchValue({
      id: pfLoanApplication.id,
      installmentAmount: pfLoanApplication.installmentAmount as any,
      noOfInstallment: pfLoanApplication.noOfInstallment as any,
      disbursementDate: pfLoanApplication.disbursementDate as any,
      disbursementAmount: pfLoanApplication.disbursementAmount,
      status: pfLoanApplication.status,
      pfAccountId: pfLoanApplication.pfAccountId,
    });
  }

  installmentAmountCalculation(): number {
    let installmentAmount = 0;
    const disbursementAmount = this.editForm.get(['disbursementAmount'])!.value;
    const noOfInstallment = this.editForm.get(['noOfInstallment'])!.value;
    if (disbursementAmount !== undefined && noOfInstallment !== undefined) {
      installmentAmount = Math.ceil(disbursementAmount / noOfInstallment);
      const installmentAmountFormControl = this.editForm.get('installmentAmount');
      if (installmentAmount !== null) {
        installmentAmountFormControl!.setValue(installmentAmount as any);
      }
    }
    if (disbursementAmount !== undefined) {
      this.loanDisbursementAmountValidation(disbursementAmount);
    }
    return installmentAmount;
  }

  loanDisbursementAmountValidation(disbursementAmount: number): void {
    const disbursementAmountFormControl = this.editForm.get('disbursementAmount');
    if (this.pfLoanEligibleAmount && disbursementAmount > this.pfLoanEligibleAmount) {
      disbursementAmountFormControl!.setValidators([Validators.required, Validators.min(0), Validators.max(this.pfLoanEligibleAmount)]);
    } else {
      disbursementAmountFormControl!.clearAsyncValidators();
    }
    disbursementAmountFormControl!.updateValueAndValidity();
  }

  disbursementDateValidation(): void {
    this.startRange = dayjs();
    this.endRange = this.editForm.get(['disbursementDate'])!.value;
    this.isConflict = false;
    this.isDateInvalid = false;
    this.isDaysEqualZero = false;

    if (this.editForm.get(['disbursementDate'])!.value !== null) {
      if (this.startRange !== undefined && this.endRange !== undefined) {
        this.durationWithoutCalc = -this.startRange.diff(this.endRange, 'days') + 1;
        if (this.durationWithoutCalc < 1) {
          this.isDateInvalid = true;
          return;
        }
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    // this.isSaving = true;
    // const pfLoanApplication = this.createFromForm();
    // if (pfLoanApplication.id !== undefined) {
    //   this.subscribeToSaveResponse(this.pfLoanApplicationService.update(pfLoanApplication));
    // } else {
    //   this.subscribeToSaveResponse(this.pfLoanApplicationService.create(pfLoanApplication));
    // }
  }

  private createFromForm(): IPfLoanApplication {
    return {
      ...new PfLoanApplication(),
      id: this.editForm.get(['id'])!.value,
      installmentAmount: this.installmentAmountCalculation(),
      noOfInstallment: this.editForm.get(['noOfInstallment'])!.value,

      disbursementDate: this.editForm.get(['disbursementDate'])!.value,
      disbursementAmount: this.editForm.get(['disbursementAmount'])!.value,
      status: Status.PENDING as any,

      pfAccountId: this.pfAccounts[0].id,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoanApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
