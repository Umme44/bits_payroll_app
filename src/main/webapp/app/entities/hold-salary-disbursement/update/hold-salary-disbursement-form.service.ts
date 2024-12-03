import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IHoldSalaryDisbursement, NewHoldSalaryDisbursement } from '../hold-salary-disbursement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHoldSalaryDisbursement for edit and NewHoldSalaryDisbursementFormGroupInput for create.
 */
type HoldSalaryDisbursementFormGroupInput = IHoldSalaryDisbursement | PartialWithRequiredKeyOf<NewHoldSalaryDisbursement>;

type HoldSalaryDisbursementFormDefaults = Pick<NewHoldSalaryDisbursement, 'id'>;

type HoldSalaryDisbursementFormGroupContent = {
  id: FormControl<IHoldSalaryDisbursement['id'] | NewHoldSalaryDisbursement['id']>;
  date: FormControl<IHoldSalaryDisbursement['date']>;
  userId: FormControl<IHoldSalaryDisbursement['userId']>;
  employeeSalaryId: FormControl<IHoldSalaryDisbursement['employeeSalaryId']>;
};

export type HoldSalaryDisbursementFormGroup = FormGroup<HoldSalaryDisbursementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HoldSalaryDisbursementFormService {
  createHoldSalaryDisbursementFormGroup(
    holdSalaryDisbursement: HoldSalaryDisbursementFormGroupInput = { id: null }
  ): HoldSalaryDisbursementFormGroup {
    const holdSalaryDisbursementRawValue = {
      ...this.getFormDefaults(),
      ...holdSalaryDisbursement,
    };
    return new FormGroup<HoldSalaryDisbursementFormGroupContent>({
      id: new FormControl(
        { value: holdSalaryDisbursementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(holdSalaryDisbursementRawValue.date, {
        validators: [Validators.required],
      }),
      userId: new FormControl(holdSalaryDisbursementRawValue.userId),
      employeeSalaryId: new FormControl(holdSalaryDisbursementRawValue.employeeSalaryId),
    });
  }

  getHoldSalaryDisbursement(form: HoldSalaryDisbursementFormGroup): IHoldSalaryDisbursement | NewHoldSalaryDisbursement {
    return form.getRawValue() as IHoldSalaryDisbursement | NewHoldSalaryDisbursement;
  }

  resetForm(form: HoldSalaryDisbursementFormGroup, holdSalaryDisbursement: HoldSalaryDisbursementFormGroupInput): void {
    const holdSalaryDisbursementRawValue = { ...this.getFormDefaults(), ...holdSalaryDisbursement };
    form.reset(
      {
        ...holdSalaryDisbursementRawValue,
        id: { value: holdSalaryDisbursementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HoldSalaryDisbursementFormDefaults {
    return {
      id: null,
    };
  }
}
