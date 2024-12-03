import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConfig, NewConfig } from '../config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConfig for edit and NewConfigFormGroupInput for create.
 */
type ConfigFormGroupInput = IConfig | PartialWithRequiredKeyOf<NewConfig>;

type ConfigFormDefaults = Pick<NewConfig, 'id'>;

type ConfigFormGroupContent = {
  id: FormControl<IConfig['id'] | NewConfig['id']>;
  key: FormControl<IConfig['key']>;
  value: FormControl<IConfig['value']>;
};

export type ConfigFormGroup = FormGroup<ConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConfigFormService {
  createConfigFormGroup(config: ConfigFormGroupInput = { id: null }): ConfigFormGroup {
    const configRawValue = {
      ...this.getFormDefaults(),
      ...config,
    };
    return new FormGroup<ConfigFormGroupContent>({
      id: new FormControl(
        { value: configRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      key: new FormControl(configRawValue.key, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(255)],
      }),
      value: new FormControl(configRawValue.value, {
        validators: [Validators.minLength(1), Validators.maxLength(255)],
      }),
    });
  }

  getConfig(form: ConfigFormGroup): IConfig | NewConfig {
    return form.getRawValue() as IConfig | NewConfig;
  }

  resetForm(form: ConfigFormGroup, config: ConfigFormGroupInput): void {
    const configRawValue = { ...this.getFormDefaults(), ...config };
    form.reset(
      {
        ...configRawValue,
        id: { value: configRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConfigFormDefaults {
    return {
      id: null,
    };
  }
}
