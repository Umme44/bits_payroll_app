import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIncomeTaxChallan, NewIncomeTaxChallan } from '../income-tax-challan.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncomeTaxChallan for edit and NewIncomeTaxChallanFormGroupInput for create.
 */
type IncomeTaxChallanFormGroupInput = IIncomeTaxChallan | PartialWithRequiredKeyOf<NewIncomeTaxChallan>;

type IncomeTaxChallanFormDefaults = Pick<NewIncomeTaxChallan, 'id'>;

type IncomeTaxChallanFormGroupContent = {
  id: FormControl<IIncomeTaxChallan['id'] | NewIncomeTaxChallan['id']>;
  challanNo: FormControl<IIncomeTaxChallan['challanNo']>;
  challanDate: FormControl<IIncomeTaxChallan['challanDate']>;
  amount: FormControl<IIncomeTaxChallan['amount']>;
  month: FormControl<IIncomeTaxChallan['month']>;
  year: FormControl<IIncomeTaxChallan['year']>;
  remarks: FormControl<IIncomeTaxChallan['remarks']>;
  aitConfig: FormControl<IIncomeTaxChallan['aitConfig']>;
  aitConfigId: FormControl<IIncomeTaxChallan['aitConfigId']>;
};

export type IncomeTaxChallanFormGroup = FormGroup<IncomeTaxChallanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncomeTaxChallanFormService {
  createIncomeTaxChallanFormGroup(incomeTaxChallan: IncomeTaxChallanFormGroupInput = { id: null }): IncomeTaxChallanFormGroup {
    const incomeTaxChallanRawValue = { ...this.getFormDefaults(), ...incomeTaxChallan };
    return new FormGroup<IncomeTaxChallanFormGroupContent>({
      id: new FormControl({ value: incomeTaxChallanRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      challanNo: new FormControl(incomeTaxChallanRawValue.challanNo, { validators: [Validators.required, Validators.maxLength(200), CustomValidator.alphaNumericValidator()]}),
      challanDate: new FormControl(incomeTaxChallanRawValue.challanDate, { validators: [Validators.required] }),
      amount: new FormControl(incomeTaxChallanRawValue.amount, { validators: [Validators.required] }),
      month: new FormControl(incomeTaxChallanRawValue.month, { validators: [Validators.required] }),
      year: new FormControl(incomeTaxChallanRawValue.year, {
        validators: [Validators.required, Validators.min(1990), Validators.max(2199)],
      }),
      remarks: new FormControl(incomeTaxChallanRawValue.remarks, { validators: [Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()] }),
      aitConfig: new FormControl(incomeTaxChallanRawValue.aitConfig),
      aitConfigId: new FormControl(incomeTaxChallanRawValue.aitConfigId, { validators: [Validators.required] }),
    });
  }

  getIncomeTaxChallan(form: IncomeTaxChallanFormGroup): IIncomeTaxChallan | NewIncomeTaxChallan {
    return form.getRawValue() as IIncomeTaxChallan | NewIncomeTaxChallan;
  }

  resetForm(form: IncomeTaxChallanFormGroup, incomeTaxChallan: IncomeTaxChallanFormGroupInput): void {
    const incomeTaxChallanRawValue = { ...this.getFormDefaults(), ...incomeTaxChallan };
    form.reset({ ...incomeTaxChallanRawValue, id: { value: incomeTaxChallanRawValue.id, disabled: true } } as any);
  }

  private getFormDefaults(): IncomeTaxChallanFormDefaults {
    return { id: null };
  }
}
