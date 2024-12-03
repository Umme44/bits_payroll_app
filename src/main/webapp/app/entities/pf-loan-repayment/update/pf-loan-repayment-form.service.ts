import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPfLoanRepayment, NewPfLoanRepayment } from '../pf-loan-repayment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfLoanRepayment for edit and NewPfLoanRepaymentFormGroupInput for create.
 */
type PfLoanRepaymentFormGroupInput = IPfLoanRepayment | PartialWithRequiredKeyOf<NewPfLoanRepayment>;

type PfLoanRepaymentFormDefaults = Pick<NewPfLoanRepayment, 'id'>;

type PfLoanRepaymentFormGroupContent = {
  id: FormControl<IPfLoanRepayment['id'] | NewPfLoanRepayment['id']>;
  amount: FormControl<IPfLoanRepayment['amount']>;
  status: FormControl<IPfLoanRepayment['status']>;
  deductionMonth: FormControl<IPfLoanRepayment['deductionMonth']>;
  deductionYear: FormControl<IPfLoanRepayment['deductionYear']>;
  deductionDate: FormControl<IPfLoanRepayment['deductionDate']>;
  pfLoanId: FormControl<IPfLoanRepayment['pfLoanId']>;
};

export type PfLoanRepaymentFormGroup = FormGroup<PfLoanRepaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfLoanRepaymentFormService {
  createPfLoanRepaymentFormGroup(pfLoanRepayment: PfLoanRepaymentFormGroupInput = { id: null }): PfLoanRepaymentFormGroup {
    const pfLoanRepaymentRawValue = {
      ...this.getFormDefaults(),
      ...pfLoanRepayment,
    };
    return new FormGroup<PfLoanRepaymentFormGroupContent>({
      id: new FormControl(
        { value: pfLoanRepaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      amount: new FormControl(pfLoanRepaymentRawValue.amount),
      status: new FormControl(pfLoanRepaymentRawValue.status),
      deductionMonth: new FormControl(pfLoanRepaymentRawValue.deductionMonth),
      deductionYear: new FormControl(pfLoanRepaymentRawValue.deductionYear),
      deductionDate: new FormControl(pfLoanRepaymentRawValue.deductionDate),
      pfLoanId: new FormControl(pfLoanRepaymentRawValue.pfLoanId),
    });
  }

  getPfLoanRepayment(form: PfLoanRepaymentFormGroup): IPfLoanRepayment | NewPfLoanRepayment {
    return form.getRawValue() as IPfLoanRepayment | NewPfLoanRepayment;
  }

  resetForm(form: PfLoanRepaymentFormGroup, pfLoanRepayment: PfLoanRepaymentFormGroupInput): void {
    const pfLoanRepaymentRawValue = { ...this.getFormDefaults(), ...pfLoanRepayment };
    form.reset(
      {
        ...pfLoanRepaymentRawValue,
        id: { value: pfLoanRepaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfLoanRepaymentFormDefaults {
    return {
      id: null,
    };
  }
}
