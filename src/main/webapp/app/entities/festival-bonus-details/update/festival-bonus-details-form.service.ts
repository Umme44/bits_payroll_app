import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFestivalBonusDetails, NewFestivalBonusDetails } from '../festival-bonus-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFestivalBonusDetails for edit and NewFestivalBonusDetailsFormGroupInput for create.
 */
type FestivalBonusDetailsFormGroupInput = IFestivalBonusDetails | PartialWithRequiredKeyOf<NewFestivalBonusDetails>;

type FestivalBonusDetailsFormDefaults = Pick<NewFestivalBonusDetails, 'id' | 'isHold'>;

type FestivalBonusDetailsFormGroupContent = {
  id: FormControl<IFestivalBonusDetails['id'] | NewFestivalBonusDetails['id']>;
  bonusAmount: FormControl<IFestivalBonusDetails['bonusAmount']>;
  remarks: FormControl<IFestivalBonusDetails['remarks']>;
  isHold: FormControl<IFestivalBonusDetails['isHold']>;
  basic: FormControl<IFestivalBonusDetails['basic']>;
  gross: FormControl<IFestivalBonusDetails['gross']>;
  employee: FormControl<IFestivalBonusDetails['employee']>;
  festival: FormControl<IFestivalBonusDetails['festival']>;
};

export type FestivalBonusDetailsFormGroup = FormGroup<FestivalBonusDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FestivalBonusDetailsFormService {
  createFestivalBonusDetailsFormGroup(
    festivalBonusDetails: FestivalBonusDetailsFormGroupInput = { id: null }
  ): FestivalBonusDetailsFormGroup {
    const festivalBonusDetailsRawValue = {
      ...this.getFormDefaults(),
      ...festivalBonusDetails,
    };
    return new FormGroup<FestivalBonusDetailsFormGroupContent>({
      id: new FormControl(
        { value: festivalBonusDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      bonusAmount: new FormControl(festivalBonusDetailsRawValue.bonusAmount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      remarks: new FormControl(festivalBonusDetailsRawValue.remarks, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      isHold: new FormControl(festivalBonusDetailsRawValue.isHold, {
        validators: [Validators.required],
      }),
      basic: new FormControl(festivalBonusDetailsRawValue.basic),
      gross: new FormControl(festivalBonusDetailsRawValue.gross),
      employee: new FormControl(festivalBonusDetailsRawValue.employee, {
        validators: [Validators.required],
      }),
      festival: new FormControl(festivalBonusDetailsRawValue.festival, {
        validators: [Validators.required],
      }),
    });
  }

  getFestivalBonusDetails(form: FestivalBonusDetailsFormGroup): IFestivalBonusDetails | NewFestivalBonusDetails {
    return form.getRawValue() as IFestivalBonusDetails | NewFestivalBonusDetails;
  }

  resetForm(form: FestivalBonusDetailsFormGroup, festivalBonusDetails: FestivalBonusDetailsFormGroupInput): void {
    const festivalBonusDetailsRawValue = { ...this.getFormDefaults(), ...festivalBonusDetails };
    form.reset(
      {
        ...festivalBonusDetailsRawValue,
        id: { value: festivalBonusDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FestivalBonusDetailsFormDefaults {
    return {
      id: null,
      isHold: false,
    };
  }
}
