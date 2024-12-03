import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFestivalBonusConfig, NewFestivalBonusConfig } from '../festival-bonus-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFestivalBonusConfig for edit and NewFestivalBonusConfigFormGroupInput for create.
 */
type FestivalBonusConfigFormGroupInput = IFestivalBonusConfig | PartialWithRequiredKeyOf<NewFestivalBonusConfig>;

type FestivalBonusConfigFormDefaults = Pick<NewFestivalBonusConfig, 'id'>;

type FestivalBonusConfigFormGroupContent = {
  id: FormControl<IFestivalBonusConfig['id'] | NewFestivalBonusConfig['id']>;
  employeeCategory: FormControl<IFestivalBonusConfig['employeeCategory']>;
  percentageFromGross: FormControl<IFestivalBonusConfig['percentageFromGross']>;
};

export type FestivalBonusConfigFormGroup = FormGroup<FestivalBonusConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FestivalBonusConfigFormService {
  createFestivalBonusConfigFormGroup(festivalBonusConfig: FestivalBonusConfigFormGroupInput = { id: null }): FestivalBonusConfigFormGroup {
    const festivalBonusConfigRawValue = {
      ...this.getFormDefaults(),
      ...festivalBonusConfig,
    };
    return new FormGroup<FestivalBonusConfigFormGroupContent>({
      id: new FormControl(
        { value: festivalBonusConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeCategory: new FormControl(festivalBonusConfigRawValue.employeeCategory, {
        validators: [Validators.required],
      }),
      percentageFromGross: new FormControl(festivalBonusConfigRawValue.percentageFromGross, {
        validators: [Validators.required, Validators.min(0), Validators.max(5000)],
      }),
    });
  }

  getFestivalBonusConfig(form: FestivalBonusConfigFormGroup): IFestivalBonusConfig | NewFestivalBonusConfig {
    return form.getRawValue() as IFestivalBonusConfig | NewFestivalBonusConfig;
  }

  resetForm(form: FestivalBonusConfigFormGroup, festivalBonusConfig: FestivalBonusConfigFormGroupInput): void {
    const festivalBonusConfigRawValue = { ...this.getFormDefaults(), ...festivalBonusConfig };
    form.reset(
      {
        ...festivalBonusConfigRawValue,
        id: { value: festivalBonusConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FestivalBonusConfigFormDefaults {
    return {
      id: null,
    };
  }
}
