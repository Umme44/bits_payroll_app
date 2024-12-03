import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHoldFbDisbursement, NewHoldFbDisbursement } from '../hold-fb-disbursement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHoldFbDisbursement for edit and NewHoldFbDisbursementFormGroupInput for create.
 */
type HoldFbDisbursementFormGroupInput = IHoldFbDisbursement | PartialWithRequiredKeyOf<NewHoldFbDisbursement>;

type HoldFbDisbursementFormDefaults = Pick<NewHoldFbDisbursement, 'id'>;

type HoldFbDisbursementFormGroupContent = {
  id: FormControl<IHoldFbDisbursement['id'] | NewHoldFbDisbursement['id']>;
  disbursedAt: FormControl<IHoldFbDisbursement['disbursedAt']>;
  remarks: FormControl<IHoldFbDisbursement['remarks']>;
  disbursedById: FormControl<IHoldFbDisbursement['disbursedById']>;
  festivalBonusDetailId: FormControl<IHoldFbDisbursement['festivalBonusDetailId']>;
};

export type HoldFbDisbursementFormGroup = FormGroup<HoldFbDisbursementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HoldFbDisbursementFormService {
  createHoldFbDisbursementFormGroup(holdFbDisbursement: HoldFbDisbursementFormGroupInput = { id: null }): HoldFbDisbursementFormGroup {
    const holdFbDisbursementRawValue = {
      ...this.getFormDefaults(),
      ...holdFbDisbursement,
    };
    return new FormGroup<HoldFbDisbursementFormGroupContent>({
      id: new FormControl(
        { value: holdFbDisbursementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      disbursedAt: new FormControl(holdFbDisbursementRawValue.disbursedAt, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(holdFbDisbursementRawValue.remarks, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      disbursedById: new FormControl(holdFbDisbursementRawValue.disbursedById, {
        validators: [Validators.required],
      }),
      festivalBonusDetailId: new FormControl(holdFbDisbursementRawValue.festivalBonusDetailId, {
        validators: [Validators.required],
      }),
    });
  }

  getHoldFbDisbursement(form: HoldFbDisbursementFormGroup): IHoldFbDisbursement | NewHoldFbDisbursement {
    return form.getRawValue() as IHoldFbDisbursement | NewHoldFbDisbursement;
  }

  resetForm(form: HoldFbDisbursementFormGroup, holdFbDisbursement: HoldFbDisbursementFormGroupInput): void {
    const holdFbDisbursementRawValue = { ...this.getFormDefaults(), ...holdFbDisbursement };
    form.reset(
      {
        ...holdFbDisbursementRawValue,
        id: { value: holdFbDisbursementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HoldFbDisbursementFormDefaults {
    return {
      id: null,
    };
  }
}
