import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInsuranceConfiguration, NewInsuranceConfiguration } from '../insurance-configuration.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsuranceConfiguration for edit and NewInsuranceConfigurationFormGroupInput for create.
 */
type InsuranceConfigurationFormGroupInput = IInsuranceConfiguration | PartialWithRequiredKeyOf<NewInsuranceConfiguration>;

type InsuranceConfigurationFormDefaults = Pick<NewInsuranceConfiguration, 'id'>;

type InsuranceConfigurationFormGroupContent = {
  id: FormControl<IInsuranceConfiguration['id'] | NewInsuranceConfiguration['id']>;
  maxTotalClaimLimitPerYear: FormControl<IInsuranceConfiguration['maxTotalClaimLimitPerYear']>;
  maxAllowedChildAge: FormControl<IInsuranceConfiguration['maxAllowedChildAge']>;
  insuranceClaimLink: FormControl<IInsuranceConfiguration['insuranceClaimLink']>;
};

export type InsuranceConfigurationFormGroup = FormGroup<InsuranceConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsuranceConfigurationFormService {
  createInsuranceConfigurationFormGroup(
    insuranceConfiguration: InsuranceConfigurationFormGroupInput = { id: null }
  ): InsuranceConfigurationFormGroup {
    const insuranceConfigurationRawValue = {
      ...this.getFormDefaults(),
      ...insuranceConfiguration,
    };
    return new FormGroup<InsuranceConfigurationFormGroupContent>({
      id: new FormControl(
        { value: insuranceConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      maxTotalClaimLimitPerYear: new FormControl(insuranceConfigurationRawValue.maxTotalClaimLimitPerYear, {
        validators: [Validators.required],
      }),
      maxAllowedChildAge: new FormControl(insuranceConfigurationRawValue.maxAllowedChildAge),
      insuranceClaimLink: new FormControl(insuranceConfigurationRawValue.insuranceClaimLink, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
    });
  }

  getInsuranceConfiguration(form: InsuranceConfigurationFormGroup): IInsuranceConfiguration | NewInsuranceConfiguration {
    return form.getRawValue() as IInsuranceConfiguration | NewInsuranceConfiguration;
  }

  resetForm(form: InsuranceConfigurationFormGroup, insuranceConfiguration: InsuranceConfigurationFormGroupInput): void {
    const insuranceConfigurationRawValue = { ...this.getFormDefaults(), ...insuranceConfiguration };
    form.reset(
      {
        ...insuranceConfigurationRawValue,
        id: { value: insuranceConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InsuranceConfigurationFormDefaults {
    return {
      id: null,
    };
  }
}
