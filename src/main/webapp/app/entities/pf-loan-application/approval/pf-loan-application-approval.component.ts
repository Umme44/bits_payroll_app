import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { PfLoanApplicationFormApprovalService } from './pf-loan-application-approval.service';
import { IPfAccount } from '../../pf-account/pf-account.model';
import { IPfLoanApplication } from '../pf-loan-application.model';
import { EmployeeBankDetails, IEmployeeBankDetails } from '../../../shared/model/employee-bank-details.model';
import { PfAccountService } from '../../pf-account/service/pf-account.service';
import { EmployeeService } from '../../employee/service/employee.service';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';
import { IPfLoanApplicationForm, PfLoanApplicationForm } from './pf-loan-application-form.model';
import { Status } from '../../enumerations/status.model';
import { IEmployee } from '../../employee/employee.model';

type SelectableEntity = IEmployee | IPfAccount;

@Component({
  selector: 'jhi-pf-loan-application-approval',
  templateUrl: './pf-loan-application-approval.component.html',
})
export class PfLoanApplicationApprovalComponent implements OnInit {
  today = new Date();

  isSaving = false;
  pfLoanApplication!: IPfLoanApplication;
  pfAccount?: IPfAccount;
  employees: IEmployee[] = [];
  employeeBankDetails!: IEmployeeBankDetails;
  pfAccounts: IPfAccount[] = [];
  bankSelector = false;
  chequeSelector = false;
  selectedOption!: string;
  isRecommendedChecked = false;
  pfLoanEligibleAmount = 0;

  editForm = this.fb.group({
    pfLoanApplicationId: [],
    instalmentStartFrom: [null, Validators.required],
    installmentAmount: [null, [Validators.min(1)]],
    noOfInstallment: [null, [Validators.required, Validators.min(1), Validators.max(24)]],
    remarks: [],
    isRecommended: [],
    recommendationDate: [],
    isApproved: [],
    approvalDate: [null, [Validators.required]],
    disbursementDate: [null, Validators.required],
    disbursementAmount: [null, [Validators.required, Validators.min(0)]],
    status: [],
    recommendedById: [],
    approvedById: [],
    pfAccountId: [],
    bankName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250), Validators.pattern('^[a-zA-Z.\\s]+$')]],
    bankAccountNumber: [
      null,
      [Validators.required, Validators.minLength(9), Validators.maxLength(250), Validators.pattern('^[a-zA-Z0-9.\\s]+$')],
    ],
    bankBranch: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250), Validators.pattern('^[a-zA-Z0-9.\\s]+$')]],
    chequeNumber: [
      null,
      [Validators.required, Validators.minLength(10), Validators.maxLength(250), Validators.pattern('^[a-zA-Z0-9.\\s]+$')],
    ],

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

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body || [];
        /*employee select drop down with ng-select*/
        this.employees = this.employees.map(item => {
          return {
            id: item.id,
            pin: item.pin,
            name: item.fullName,
            designation: item.designationName as any,
            fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
          };
        });
      });

      if (pfLoanApplication.pfAccountId) {
        this.pfLoanApplicationService.getPfLoanApplicationEligibility(pfLoanApplication.pfAccountId).subscribe(response => {
          this.pfLoanEligibleAmount = Number(response.body!.pfLoanEligibleAmount);
          this.loanDisbursementAmountValidation();
        });
      }

      this.pfLoanApplicationService
        .getEmployeeBankDetailsByPin(pfLoanApplication.pin)
        .subscribe((employeeBankDetails: HttpResponse<EmployeeBankDetails>) => {
          if (employeeBankDetails.body) {
            this.employeeBankDetails = employeeBankDetails.body;
            this.updateForm(pfLoanApplication, employeeBankDetails.body);

            /*if isRecommeded pre selected, show recommended related fields*/
            if (pfLoanApplication.isRecommended) {
              this.isRecommendedChecked = true;
            }
          }
        });
    });
  }

  updateForm(pfLoanApplication: IPfLoanApplication, bankDetails: IEmployeeBankDetails): void {
    this.isRecommendedChecked = pfLoanApplication.isRecommended!;

    this.editForm.patchValue({
      pfLoanApplicationId: pfLoanApplication.id,
      installmentAmount: pfLoanApplication.installmentAmount as any,
      noOfInstallment: pfLoanApplication.noOfInstallment as any,
      remarks: pfLoanApplication.remarks,
      isRecommended: pfLoanApplication.isRecommended,
      recommendationDate: pfLoanApplication.recommendationDate,
      isApproved: pfLoanApplication.isApproved,
      approvalDate: pfLoanApplication.approvalDate as any,
      disbursementDate: pfLoanApplication.disbursementDate as any,
      disbursementAmount: pfLoanApplication.disbursementAmount as any,
      recommendedById: pfLoanApplication.recommendedById,
      approvedById: pfLoanApplication.approvedById,
      pfAccountId: pfLoanApplication.pfAccountId,

      bankName: bankDetails.bankName as any,
      bankAccountNumber: bankDetails.bankAccountNumber as any,
      bankBranch: bankDetails.bankBranch as any,
    });
  }

  isRecommendedChange(event: any): void {
    this.isRecommendedChecked = event.target.checked;
  }

  bankAndCheckValidation($event: any): void {
    const chequeNumberFormControl = this.editForm.get('chequeNumber');
    const bankNameFormControl = this.editForm.get('bankName');
    const bankAccountNumberFormControl = this.editForm.get('bankAccountNumber');
    const bankBranchFormControl = this.editForm.get('bankBranch');

    this.selectedOption = $event.target.value;
    if (this.selectedOption === 'bank') {
      this.bankSelector = true;
      this.chequeSelector = false;

      bankNameFormControl!.setValidators([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z.\\s]+$'),
      ]);
      bankAccountNumberFormControl!.setValidators([
        Validators.required,
        Validators.minLength(9),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s\\-]+$'),
      ]);
      bankBranchFormControl!.setValidators([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s]+$'),
      ]);

      chequeNumberFormControl!.clearValidators();
    } else if (this.selectedOption === 'cheque') {
      this.chequeSelector = true;
      this.bankSelector = false;

      chequeNumberFormControl!.setValidators([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s]+$'),
      ]);
      bankNameFormControl!.clearValidators();
      bankAccountNumberFormControl!.clearValidators();
      bankBranchFormControl!.clearValidators();
    }
    bankNameFormControl!.updateValueAndValidity();
    bankAccountNumberFormControl!.updateValueAndValidity();
    bankBranchFormControl!.updateValueAndValidity();
    chequeNumberFormControl!.updateValueAndValidity();
  }

  isBankInfoMissing(): boolean {
    //check employee's bank info is missing
    if (
      this.employeeBankDetails.bankAccountNumber === null ||
      this.employeeBankDetails.bankAccountNumber === '' ||
      this.employeeBankDetails.bankName === '' ||
      this.employeeBankDetails.bankName === null ||
      this.employeeBankDetails.bankBranch === '' ||
      this.employeeBankDetails.bankBranch === null
    ) {
      return true;
    }
    return false;
  }

  previousState(): void {
    window.history.back();
  }

  private createFromForm(): IPfLoanApplicationForm {
    return {
      ...new PfLoanApplicationForm(),
      pfLoanApplicationId: this.pfLoanApplication.id,
      instalmentStartFrom: this.editForm.get(['instalmentStartFrom'])!.value,
      installmentAmount: this.editForm.get(['installmentAmount'])!.value,
      noOfInstallment: this.editForm.get(['noOfInstallment'])!.value,
      isRecommended: this.editForm.get(['isRecommended'])!.value,
      recommendationDate: this.editForm.get(['recommendationDate'])!.value,
      approvalDate: this.editForm.get(['approvalDate'])!.value,
      disbursementDate: this.editForm.get(['disbursementDate'])!.value,
      disbursementAmount: this.editForm.get(['disbursementAmount'])!.value,

      status: Status.APPROVED,
      recommendedById: this.editForm.get(['recommendedById'])!.value,
      approvedById: this.editForm.get(['approvedById'])!.value,

      bankName: this.editForm.get(['bankName'])!.value,
      bankBranch: this.editForm.get(['bankBranch'])!.value,
      bankAccountNumber: this.editForm.get(['bankAccountNumber'])!.value,
      chequeNumber: this.editForm.get(['chequeNumber'])!.value,
      pfAccountId: this.pfLoanApplication.pfAccountId,
      accHolderName: this.pfAccount?.accHolderName,
      pfCode: this.pfLoanApplication.pfCode,
      pin: this.pfLoanApplication.pin,
    };
  }

  installmentAmountCalculation(): void {
    const disbursementAmount = this.editForm.get(['disbursementAmount'])!.value;
    const noOfInstallment = this.editForm.get(['noOfInstallment'])!.value;
    if (disbursementAmount !== undefined && noOfInstallment !== undefined) {
      const installmentAmount = Math.ceil(disbursementAmount / noOfInstallment);
      if (this.editForm.get('installmentAmount') !== null) {
        this.editForm.get('installmentAmount')!.setValue(installmentAmount as any);
      }
    }
  }

  loanDisbursementAmountValidation(): void {
    const disbursementAmountFormControl = this.editForm.get('disbursementAmount');
    disbursementAmountFormControl!.setValidators([Validators.required, Validators.min(0), Validators.max(this.pfLoanEligibleAmount)]);
    //disbursementAmountFormControl!.updateValueAndValidity();
  }

  save(): void {
    this.isSaving = true;
    const pfLoanApplicationForm = this.createFromForm();
    this.subscribeToSaveResponse(this.pfLoanApplicationFormApprovalService.approvePfLoanApplication(pfLoanApplicationForm));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoanApplicationForm>>): void {
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
