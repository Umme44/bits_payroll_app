import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArrearPayment, NewArrearPayment } from '../arrear-payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArrearPayment for edit and NewArrearPaymentFormGroupInput for create.
 */
type ArrearPaymentFormGroupInput = IArrearPayment | PartialWithRequiredKeyOf<NewArrearPayment>;

type ArrearPaymentFormDefaults = Pick<NewArrearPayment, 'id' | 'isDeleted' | 'deductTaxUponPayment'>;

type ArrearPaymentFormGroupContent = {
  id: FormControl<IArrearPayment['id'] | NewArrearPayment['id']>;
  paymentType: FormControl<IArrearPayment['paymentType']>;
  disbursementDate: FormControl<IArrearPayment['disbursementDate']>;
  salaryMonth: FormControl<IArrearPayment['salaryMonth']>;
  salaryYear: FormControl<IArrearPayment['salaryYear']>;
  approvalStatus: FormControl<IArrearPayment['approvalStatus']>;
  disbursementAmount: FormControl<IArrearPayment['disbursementAmount']>;
  isDeleted: FormControl<IArrearPayment['isDeleted']>;
  arrearPF: FormControl<IArrearPayment['arrearPF']>;
  taxDeduction: FormControl<IArrearPayment['taxDeduction']>;
  deductTaxUponPayment: FormControl<IArrearPayment['deductTaxUponPayment']>;
  arrearSalaryItem: FormControl<IArrearPayment['arrearSalaryItem']>;
};

export type ArrearPaymentFormGroup = FormGroup<ArrearPaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArrearPaymentFormService {
  createArrearPaymentFormGroup(arrearPayment: ArrearPaymentFormGroupInput = { id: null }): ArrearPaymentFormGroup {
    const arrearPaymentRawValue = {
      ...this.getFormDefaults(),
      ...arrearPayment,
    };
    return new FormGroup<ArrearPaymentFormGroupContent>({
      id: new FormControl(
        { value: arrearPaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      paymentType: new FormControl(arrearPaymentRawValue.paymentType, {
        validators: [Validators.required],
      }),
      disbursementDate: new FormControl(arrearPaymentRawValue.disbursementDate),
      salaryMonth: new FormControl(arrearPaymentRawValue.salaryMonth),
      salaryYear: new FormControl(arrearPaymentRawValue.salaryYear, {
        validators: [Validators.min(1900), Validators.max(2277)],
      }),
      approvalStatus: new FormControl(arrearPaymentRawValue.approvalStatus),
      disbursementAmount: new FormControl(arrearPaymentRawValue.disbursementAmount, {
        validators: [Validators.required, Validators.min(1), Validators.max(100000000)],
      }),
      isDeleted: new FormControl(arrearPaymentRawValue.isDeleted, {
        validators: [Validators.required],
      }),
      arrearPF: new FormControl(arrearPaymentRawValue.arrearPF, {
        validators: [Validators.required, Validators.min(0), Validators.max(100000000)],
      }),
      taxDeduction: new FormControl(arrearPaymentRawValue.taxDeduction, {
        validators: [Validators.required, Validators.min(0), Validators.max(100000000)],
      }),
      deductTaxUponPayment: new FormControl(arrearPaymentRawValue.deductTaxUponPayment, {
        validators: [Validators.required],
      }),
      arrearSalaryItem: new FormControl(arrearPaymentRawValue.arrearSalaryItem, {
        validators: [Validators.required],
      }),
    });
  }

  getArrearPayment(form: ArrearPaymentFormGroup): IArrearPayment | NewArrearPayment {
    return form.getRawValue() as IArrearPayment | NewArrearPayment;
  }

  resetForm(form: ArrearPaymentFormGroup, arrearPayment: ArrearPaymentFormGroupInput): void {
    const arrearPaymentRawValue = { ...this.getFormDefaults(), ...arrearPayment };
    form.reset(
      {
        ...arrearPaymentRawValue,
        id: { value: arrearPaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArrearPaymentFormDefaults {
    return {
      id: null,
      isDeleted: false,
      deductTaxUponPayment: false,
    };
  }
}
