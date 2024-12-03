import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEmployee } from '../../employee/employee.model';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { IPfLoanApplication } from '../pf-loan-application.model';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';
import { EmployeeService } from '../../employee/service/employee.service';
import { PfAccountService } from '../../pf-account/service/pf-account.service';
import { PfLoanApplicationFormApprovalService } from '../approval/pf-loan-application-approval.service';
import { IPfLoanApplicationForm, PfLoanApplicationForm } from '../approval/pf-loan-application-form.model';
import { Status } from '../../enumerations/status.model';

type SelectableEntity = IEmployee | IPfAccount;

@Component({
  selector: 'jhi-pf-loan-application-reject',
  templateUrl: './pf-loan-application-reject.component.html',
})
export class PfLoanApplicationRejectComponent implements OnInit {
  today = new Date();

  isSaving = false;
  pfLoanApplication!: IPfLoanApplication;
  pfAccount?: IPfAccount;
  pfAccounts: IPfAccount[] = [];

  editForm = this.fb.group({
    pfLoanApplicationId: [],
    remarks: [],
    isRejected: [],
    rejectionReason: [null, [Validators.required]],
    rejectionDate: [Validators.required],
    status: [],
    rejectedById: [],
    pfAccountId: [],

    pin: [],
    pfCode: [],
  });

  constructor(
    protected pfLoanApplicationService: PfLoanApplicationService,
    protected pfLoanApplicationFormApprovalService: PfLoanApplicationFormApprovalService,
    protected employeeService: EmployeeService,
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute,
    protected route: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanApplication }) => {
      this.pfLoanApplication = pfLoanApplication;
      this.pfAccountService
        .find(pfLoanApplication.pfAccountId)
        .subscribe((res: HttpResponse<IPfAccount>) => (this.pfAccount = res.body || undefined));
      this.updateForm(pfLoanApplication);
    });
  }

  updateForm(pfLoanApplication: IPfLoanApplication): void {
    this.editForm.patchValue({
      pfLoanApplicationId: pfLoanApplication.id,
      remarks: pfLoanApplication.remarks,

      isRejected: pfLoanApplication.isRejected,
      rejectionReason: pfLoanApplication.rejectionReason as any,
      rejectionDate: pfLoanApplication.rejectionDate as any,

      rejectedById: pfLoanApplication.rejectedById,
      pfAccountId: pfLoanApplication.pfAccountId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  private createFromForm(): IPfLoanApplicationForm {
    return {
      ...new PfLoanApplicationForm(),
      pfLoanApplicationId: this.pfLoanApplication.id,
      isRejected: this.editForm.get(['isRejected'])!.value,
      rejectionDate: this.editForm.get(['rejectionDate'])!.value,
      rejectionReason: this.editForm.get(['rejectionReason'])!.value,

      status: Status.NOT_APPROVED,
      rejectedById: this.editForm.get(['rejectedById'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,

      pfAccountId: this.pfLoanApplication.pfAccountId,
      accHolderName: this.pfAccount?.accHolderName,
      pfCode: this.pfLoanApplication.pfCode,
      pin: this.pfLoanApplication.pin,
    };
  }

  reject(): void {
    this.isSaving = true;
    const pfLoanApplicationForm = this.createFromForm();
    this.subscribeToSaveResponse(this.pfLoanApplicationFormApprovalService.rejectPfLoanApplication(pfLoanApplicationForm));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoanApplicationForm>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.route.navigate(['/pf-loan-application']);
    //this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
