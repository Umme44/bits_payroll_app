import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { NewAitPayment, IAitPayment } from '../ait-payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAitPayment for edit and NewAitPaymentFormGroupInput for create.
 */
type AitPaymentFormGroupInput = IAitPayment | PartialWithRequiredKeyOf<NewAitPayment>;

type AitPaymentFormDefaults = Pick<NewAitPayment, 'id'>;

type AitPaymentFormGroupContent = {
  id: FormControl<IAitPayment['id'] | NewAitPayment['id']>;
  date: FormControl<IAitPayment['date']>;
  amount: FormControl<IAitPayment['amount']>;
  description: FormControl<IAitPayment['description']>;
  employee: FormControl<IAitPayment['employee']>;
};

export type AitPaymentFormGroup = FormGroup<AitPaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AitPaymentFormService {
  createAitPaymentFormGroup(aitPayment: AitPaymentFormGroupInput = { id: null }): AitPaymentFormGroup {
    const aitPaymentRawValue = {
      ...this.getFormDefaults(),
      ...aitPayment,
    };
    return new FormGroup<AitPaymentFormGroupContent>({
      id: new FormControl(
        { value: aitPaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(aitPaymentRawValue.date),
      amount: new FormControl(aitPaymentRawValue.amount),
      description: new FormControl(aitPaymentRawValue.description),
      employee: new FormControl(aitPaymentRawValue.employee),
    });
  }

  getAitPayment(form: AitPaymentFormGroup): IAitPayment | NewAitPayment {
    return form.getRawValue() as IAitPayment | NewAitPayment;
  }

  resetForm(form: AitPaymentFormGroup, aitPayment: AitPaymentFormGroupInput): void {
    const aitPaymentRawValue = { ...this.getFormDefaults(), ...aitPayment };
    form.reset(
      {
        ...aitPaymentRawValue,
        id: { value: aitPaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AitPaymentFormDefaults {
    return {
      id: null,
    };
  }
}
