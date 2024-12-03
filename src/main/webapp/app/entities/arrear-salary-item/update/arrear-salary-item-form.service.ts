import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArrearSalaryItem, NewArrearSalaryItem } from '../arrear-salary-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArrearSalaryItem for edit and NewArrearSalaryItemFormGroupInput for create.
 */
type ArrearSalaryItemFormGroupInput = IArrearSalaryItem | PartialWithRequiredKeyOf<NewArrearSalaryItem>;

type ArrearSalaryItemFormDefaults = Pick<NewArrearSalaryItem, 'id' | 'hasPfArrearDeduction' | 'isFestivalBonus' | 'isDeleted'>;

type ArrearSalaryItemFormGroupContent = {
  id: FormControl<IArrearSalaryItem['id'] | NewArrearSalaryItem['id']>;
  title: FormControl<IArrearSalaryItem['title']>;
  arrearAmount: FormControl<IArrearSalaryItem['arrearAmount']>;
  hasPfArrearDeduction: FormControl<IArrearSalaryItem['hasPfArrearDeduction']>;
  pfArrearDeduction: FormControl<IArrearSalaryItem['pfArrearDeduction']>;
  isFestivalBonus: FormControl<IArrearSalaryItem['isFestivalBonus']>;
  isDeleted: FormControl<IArrearSalaryItem['isDeleted']>;
  arrearSalaryMaster: FormControl<IArrearSalaryItem['arrearSalaryMaster']>;
  employee: FormControl<IArrearSalaryItem['employee']>;
};

export type ArrearSalaryItemFormGroup = FormGroup<ArrearSalaryItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryItemFormService {
  createArrearSalaryItemFormGroup(arrearSalaryItem: ArrearSalaryItemFormGroupInput = { id: null }): ArrearSalaryItemFormGroup {
    const arrearSalaryItemRawValue = {
      ...this.getFormDefaults(),
      ...arrearSalaryItem,
    };
    return new FormGroup<ArrearSalaryItemFormGroupContent>({
      id: new FormControl(
        { value: arrearSalaryItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(arrearSalaryItemRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(255)],
      }),
      arrearAmount: new FormControl(arrearSalaryItemRawValue.arrearAmount, {
        validators: [Validators.required, Validators.min(0), Validators.max(10000000)],
      }),
      hasPfArrearDeduction: new FormControl(arrearSalaryItemRawValue.hasPfArrearDeduction),
      pfArrearDeduction: new FormControl(arrearSalaryItemRawValue.pfArrearDeduction),
      isFestivalBonus: new FormControl(arrearSalaryItemRawValue.isFestivalBonus),
      isDeleted: new FormControl(arrearSalaryItemRawValue.isDeleted, {
        validators: [Validators.required],
      }),
      arrearSalaryMaster: new FormControl(arrearSalaryItemRawValue.arrearSalaryMaster, {
        validators: [Validators.required],
      }),
      employee: new FormControl(arrearSalaryItemRawValue.employee, {
        validators: [Validators.required],
      }),
    });
  }

  getArrearSalaryItem(form: ArrearSalaryItemFormGroup): IArrearSalaryItem | NewArrearSalaryItem {
    return form.getRawValue() as IArrearSalaryItem | NewArrearSalaryItem;
  }

  resetForm(form: ArrearSalaryItemFormGroup, arrearSalaryItem: ArrearSalaryItemFormGroupInput): void {
    const arrearSalaryItemRawValue = { ...this.getFormDefaults(), ...arrearSalaryItem };
    form.reset(
      {
        ...arrearSalaryItemRawValue,
        id: { value: arrearSalaryItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArrearSalaryItemFormDefaults {
    return {
      id: null,
      hasPfArrearDeduction: false,
      isFestivalBonus: false,
      isDeleted: false,
    };
  }
}
