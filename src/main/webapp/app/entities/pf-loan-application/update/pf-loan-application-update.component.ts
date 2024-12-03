import { Component, HostListener, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PfLoanApplicationFormGroup, PfLoanApplicationFormService } from './pf-loan-application-form.service';
import { IPfLoanApplication, NewPfLoanApplication } from '../pf-loan-application.model';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';
import { Status } from 'app/entities/enumerations/status.model';
import { IEmployee } from '../../employee/employee.model';
import { IPfLoanApplicationEligible } from '../../../shared/model/pf-loan-application-eligible.model';
import { Validators } from '@angular/forms';
import { EmployeeCategory } from '../../enumerations/employee-category.model';

@Component({
  selector: 'jhi-pf-loan-application-update',
  templateUrl: './pf-loan-application-update.component.html',
})
export class PfLoanApplicationUpdateComponent implements OnInit {
  isSaving = false;
  pfLoanApplication: IPfLoanApplication | null = null;
  statusValues = Object.keys(Status);
  employees: IEmployee[] = [];
  pfAccounts: IPfAccount[] = [];

  pfAccountSelect = '';
  isRecommendedChecked = false;
  pfLoanEligibleAmount!: number;
  pfLoanApplicationEligible!: IPfLoanApplicationEligible;

  editForm: PfLoanApplicationFormGroup = this.pfLoanApplicationFormService.createPfLoanApplicationFormGroup();

  constructor(
    protected pfLoanApplicationService: PfLoanApplicationService,
    protected pfLoanApplicationFormService: PfLoanApplicationFormService,
    protected employeeService: EmployeeService,
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  comparePfAccount = (o1: IPfAccount | null, o2: IPfAccount | null): boolean => this.pfAccountService.comparePfAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanApplication }) => {
      this.pfLoanApplication = pfLoanApplication;

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body || [];

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

      this.pfAccountService.getAllPfAccountsList().subscribe((res: HttpResponse<IPfAccount[]>) => {
        this.pfAccounts = res.body || [];

        /*pf account selection field with ng-select*/
        this.pfAccounts = this.pfAccounts.map(pfAccount => {
          return {
            id: pfAccount.id,
            pin: pfAccount.pin,
            name: pfAccount.accHolderName,
            designation: pfAccount.designationName,
            accHolderName: pfAccount.pin + ' - ' + pfAccount.accHolderName + ' - ' + pfAccount.designationName,
          };
        });

        this.updateForm(pfLoanApplication);

        if (pfLoanApplication !== null) {
          //if is recommeded selected in edit page
          if (pfLoanApplication.isRecommended === true) {
            this.isRecommendedChecked = true;
          }
          /*in edit page disbale pf account selection*/
          this.editForm.get('pfAccountId')!.disable();
        }
      });
      if (pfLoanApplication) {
        this.updateForm(pfLoanApplication);
        this.editForm.get('installmentAmount').disable();
      }

      // this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfLoanApplication = this.pfLoanApplicationFormService.getPfLoanApplication(this.editForm);
    if (pfLoanApplication.id !== null) {
      this.subscribeToSaveResponse(this.pfLoanApplicationService.update(pfLoanApplication));
    } else {
      pfLoanApplication.status = Status.PENDING;
      this.subscribeToSaveResponse(this.pfLoanApplicationService.create(pfLoanApplication as NewPfLoanApplication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoanApplication>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pfLoanApplication: IPfLoanApplication): void {
    this.pfLoanApplication = pfLoanApplication;
    this.pfLoanApplicationFormService.resetForm(this.editForm, pfLoanApplication);
  }

  installmentAmountCalculation(): void {
    const disbursementAmount = this.editForm.get(['disbursementAmount'])!.value;
    const noOfInstallment = this.editForm.get(['noOfInstallment'])!.value;
    if (disbursementAmount !== undefined && noOfInstallment !== undefined) {
      const installmentAmount = Math.ceil(disbursementAmount / noOfInstallment);
      if (this.editForm.get('installmentAmount') !== null) {
        this.editForm.get('installmentAmount')!.setValue(installmentAmount);
      }
    }
    if (disbursementAmount !== undefined) {
      this.loanDisbursementAmountValidation();
    }
  }

  changePfAccount(event: any): void {
    //TODO eligible amount for pf account
    //call service to populate pfLoanApplicationForm eligibility
    const pfAccountId = event;
    if (pfAccountId !== undefined && pfAccountId !== '' && pfAccountId !== null) {
      this.pfLoanApplicationService.getPfLoanApplicationEligibility(pfAccountId).subscribe(response => {
        this.pfLoanApplicationEligible = response.body!;
        this.pfLoanEligibleAmount = Number(response.body!.pfLoanEligibleAmount);
        this.loanDisbursementAmountValidation();
      });
    }
  }

  loanDisbursementAmountValidation(): void {
    const disbursementAmountFormControl = this.editForm.get('disbursementAmount');
    if (disbursementAmountFormControl!.value > this.pfLoanEligibleAmount) {
      disbursementAmountFormControl!.setValidators([Validators.required, Validators.min(0), Validators.max(this.pfLoanEligibleAmount)]);
    } else {
      disbursementAmountFormControl!.clearValidators();
    }
    disbursementAmountFormControl!.updateValueAndValidity();
  }

  isRecommendedChange(event: any): void {
    this.isRecommendedChecked = event.target.checked;
  }

  userReadableEmployeeCategory(category: EmployeeCategory): string {
    if (category === 'REGULAR_CONFIRMED_EMPLOYEE') return 'Regular Confirmed';
    else if (category === 'REGULAR_PROVISIONAL_EMPLOYEE') return 'Regular Provisional';
    else if (category === 'CONTRACTUAL_EMPLOYEE') return 'Contractual';
    else if (category === 'INTERN') return 'Intern';
    else return '';
  }

  booleanToYesNo(b: any): string {
    if (b) return 'Yes';
    else return 'No';
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
    }
  }
}
