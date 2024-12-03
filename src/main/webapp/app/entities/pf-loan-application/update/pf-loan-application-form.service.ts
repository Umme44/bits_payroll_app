import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPfLoanApplication, NewPfLoanApplication } from '../pf-loan-application.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfLoanApplication for edit and NewPfLoanApplicationFormGroupInput for create.
 */
type PfLoanApplicationFormGroupInput = IPfLoanApplication | PartialWithRequiredKeyOf<NewPfLoanApplication>;

type PfLoanApplicationFormDefaults = Pick<NewPfLoanApplication, 'id' | 'isRecommended' | 'isApproved' | 'isRejected'>;

type PfLoanApplicationFormGroupContent = {
  id: FormControl<IPfLoanApplication['id'] | NewPfLoanApplication['id']>;
  installmentAmount: FormControl<IPfLoanApplication['installmentAmount']>;
  noOfInstallment: FormControl<IPfLoanApplication['noOfInstallment']>;
  remarks: FormControl<IPfLoanApplication['remarks']>;
  isRecommended: FormControl<IPfLoanApplication['isRecommended']>;
  recommendationDate: FormControl<IPfLoanApplication['recommendationDate']>;
  isApproved: FormControl<IPfLoanApplication['isApproved']>;
  approvalDate: FormControl<IPfLoanApplication['approvalDate']>;
  isRejected: FormControl<IPfLoanApplication['isRejected']>;
  rejectionReason: FormControl<IPfLoanApplication['rejectionReason']>;
  rejectionDate: FormControl<IPfLoanApplication['rejectionDate']>;
  disbursementDate: FormControl<IPfLoanApplication['disbursementDate']>;
  disbursementAmount: FormControl<IPfLoanApplication['disbursementAmount']>;
  status: FormControl<IPfLoanApplication['status']>;
  recommendedById: FormControl<IPfLoanApplication['recommendedById']>;
  approvedById: FormControl<IPfLoanApplication['approvedById']>;
  rejectedById: FormControl<IPfLoanApplication['rejectedById']>;
  pfAccountId: FormControl<IPfLoanApplication['pfAccountId']>;
};

export type PfLoanApplicationFormGroup = FormGroup<PfLoanApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationFormService {
  createPfLoanApplicationFormGroup(pfLoanApplication: PfLoanApplicationFormGroupInput = { id: null }): PfLoanApplicationFormGroup {
    const pfLoanApplicationRawValue = {
      ...this.getFormDefaults(),
      ...pfLoanApplication,
    };
    return new FormGroup<PfLoanApplicationFormGroupContent>({
      id: new FormControl(
        { value: pfLoanApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      installmentAmount: new FormControl(pfLoanApplicationRawValue.installmentAmount),
      noOfInstallment: new FormControl(pfLoanApplicationRawValue.noOfInstallment),
      remarks: new FormControl(pfLoanApplicationRawValue.remarks),
      isRecommended: new FormControl(pfLoanApplicationRawValue.isRecommended),
      recommendationDate: new FormControl(pfLoanApplicationRawValue.recommendationDate),
      isApproved: new FormControl(pfLoanApplicationRawValue.isApproved),
      approvalDate: new FormControl(pfLoanApplicationRawValue.approvalDate),
      isRejected: new FormControl(pfLoanApplicationRawValue.isRejected),
      rejectionReason: new FormControl(pfLoanApplicationRawValue.rejectionReason),
      rejectionDate: new FormControl(pfLoanApplicationRawValue.rejectionDate),
      disbursementDate: new FormControl(pfLoanApplicationRawValue.disbursementDate),
      disbursementAmount: new FormControl(pfLoanApplicationRawValue.disbursementAmount),
      status: new FormControl(pfLoanApplicationRawValue.status),
      recommendedById: new FormControl(pfLoanApplicationRawValue.recommendedById),
      approvedById: new FormControl(pfLoanApplicationRawValue.approvedById),
      rejectedById: new FormControl(pfLoanApplicationRawValue.rejectedById),
      pfAccountId: new FormControl(pfLoanApplicationRawValue.pfAccountId),
    });
  }

  getPfLoanApplication(form: PfLoanApplicationFormGroup): IPfLoanApplication | NewPfLoanApplication {
    return form.getRawValue() as IPfLoanApplication | NewPfLoanApplication;
  }

  resetForm(form: PfLoanApplicationFormGroup, pfLoanApplication: PfLoanApplicationFormGroupInput): void {
    const pfLoanApplicationRawValue = { ...this.getFormDefaults(), ...pfLoanApplication };
    form.reset(
      {
        ...pfLoanApplicationRawValue,
        id: { value: pfLoanApplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfLoanApplicationFormDefaults {
    return {
      id: null,
      isRecommended: false,
      isApproved: false,
      isRejected: false,
    };
  }
}
