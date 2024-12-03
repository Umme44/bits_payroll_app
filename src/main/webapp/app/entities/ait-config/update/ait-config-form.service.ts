import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAitConfig, NewAitConfig } from '../ait-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAitConfig for edit and NewAitConfigFormGroupInput for create.
 */
type AitConfigFormGroupInput = IAitConfig | PartialWithRequiredKeyOf<NewAitConfig>;

type AitConfigFormDefaults = Pick<NewAitConfig, 'id'>;

type AitConfigFormGroupContent = {
  id: FormControl<IAitConfig['id'] | NewAitConfig['id']>;
  startDate: FormControl<IAitConfig['startDate']>;
  endDate: FormControl<IAitConfig['endDate']>;
  taxConfig: FormControl<IAitConfig['taxConfig']>;
};

export type AitConfigFormGroup = FormGroup<AitConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AitConfigFormService {
  createAitConfigFormGroup(aitConfig: AitConfigFormGroupInput = { id: null }): AitConfigFormGroup {
    const aitConfigRawValue = {
      ...this.getFormDefaults(),
      ...aitConfig,
    };
    return new FormGroup<AitConfigFormGroupContent>({
      id: new FormControl(
        { value: aitConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(aitConfigRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(aitConfigRawValue.endDate, {
        validators: [Validators.required],
      }),
      taxConfig: new FormControl(aitConfigRawValue.taxConfig),
    });
  }

  getAitConfig(form: AitConfigFormGroup): IAitConfig | NewAitConfig {
    return form.getRawValue() as IAitConfig | NewAitConfig;
  }

  resetForm(form: AitConfigFormGroup, aitConfig: AitConfigFormGroupInput): void {
    const aitConfigRawValue = { ...this.getFormDefaults(), ...aitConfig };
    form.reset(
      {
        ...aitConfigRawValue,
        id: { value: aitConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AitConfigFormDefaults {
    return {
      id: null,
    };
  }
}
