import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPfLoan, NewPfLoan } from '../pf-loan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfLoan for edit and NewPfLoanFormGroupInput for create.
 */
type PfLoanFormGroupInput = IPfLoan | PartialWithRequiredKeyOf<NewPfLoan>;

type PfLoanFormDefaults = Pick<NewPfLoan, 'id'>;

type PfLoanFormGroupContent = {
  id: FormControl<IPfLoan['id'] | NewPfLoan['id']>;
  disbursementAmount: FormControl<IPfLoan['disbursementAmount']>;
  disbursementDate: FormControl<IPfLoan['disbursementDate']>;
  bankName: FormControl<IPfLoan['bankName']>;
  bankBranch: FormControl<IPfLoan['bankBranch']>;
  bankAccountNumber: FormControl<IPfLoan['bankAccountNumber']>;
  chequeNumber: FormControl<IPfLoan['chequeNumber']>;
  instalmentNumber: FormControl<IPfLoan['instalmentNumber']>;
  installmentAmount: FormControl<IPfLoan['installmentAmount']>;
  instalmentStartFrom: FormControl<IPfLoan['instalmentStartFrom']>;
  status: FormControl<IPfLoan['status']>;
  pfLoanApplication: FormControl<IPfLoan['pfLoanApplicationId']>;
  pfAccount: FormControl<IPfLoan['pfAccountId']>;
};

export type PfLoanFormGroup = FormGroup<PfLoanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfLoanFormService {
  createPfLoanFormGroup(pfLoan: PfLoanFormGroupInput = { id: null }): PfLoanFormGroup {
    const pfLoanRawValue = {
      ...this.getFormDefaults(),
      ...pfLoan,
    };
    return new FormGroup<PfLoanFormGroupContent>({
      id: new FormControl(
        { value: pfLoanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      disbursementAmount: new FormControl(pfLoanRawValue.disbursementAmount),
      disbursementDate: new FormControl(pfLoanRawValue.disbursementDate),
      bankName: new FormControl(pfLoanRawValue.bankName),
      bankBranch: new FormControl(pfLoanRawValue.bankBranch),
      bankAccountNumber: new FormControl(pfLoanRawValue.bankAccountNumber),
      chequeNumber: new FormControl(pfLoanRawValue.chequeNumber),
      instalmentNumber: new FormControl(pfLoanRawValue.instalmentNumber),
      installmentAmount: new FormControl(pfLoanRawValue.installmentAmount),
      instalmentStartFrom: new FormControl(pfLoanRawValue.instalmentStartFrom),
      status: new FormControl(pfLoanRawValue.status),
      pfLoanApplication: new FormControl(pfLoanRawValue.pfLoanApplicationId),
      pfAccount: new FormControl(pfLoanRawValue.pfAccountId),
    });
  }

  getPfLoan(form: PfLoanFormGroup): IPfLoan | NewPfLoan {
    return form.getRawValue() as IPfLoan | NewPfLoan;
  }

  resetForm(form: PfLoanFormGroup, pfLoan: PfLoanFormGroupInput): void {
    const pfLoanRawValue = { ...this.getFormDefaults(), ...pfLoan };
    form.reset(
      {
        ...pfLoanRawValue,
        id: { value: pfLoanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfLoanFormDefaults {
    return {
      id: null,
    };
  }
}
