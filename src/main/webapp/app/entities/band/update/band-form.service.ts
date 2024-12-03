import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBand, NewBand } from '../band.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBand for edit and NewBandFormGroupInput for create.
 */
type BandFormGroupInput = IBand | PartialWithRequiredKeyOf<NewBand>;

type BandFormDefaults = Pick<NewBand, 'id'>;

type BandFormGroupContent = {
  id: FormControl<IBand['id'] | NewBand['id']>;
  bandName: FormControl<IBand['bandName']>;
  minSalary: FormControl<IBand['minSalary']>;
  maxSalary: FormControl<IBand['maxSalary']>;
  welfareFund: FormControl<IBand['welfareFund']>;
  mobileCelling: FormControl<IBand['mobileCelling']>;
  createdAt: FormControl<IBand['createdAt']>;
  updatedAt: FormControl<IBand['updatedAt']>;
  createdBy: FormControl<IBand['createdBy']>;
  updatedBy: FormControl<IBand['updatedBy']>;
};

export type BandFormGroup = FormGroup<BandFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BandFormService {
  createBandFormGroup(band: BandFormGroupInput = { id: null }): BandFormGroup {
    const bandRawValue = {
      ...this.getFormDefaults(),
      ...band,
    };
    return new FormGroup<BandFormGroupContent>({
      id: new FormControl(
        { value: bandRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      bandName: new FormControl(bandRawValue.bandName, {
        validators: [CustomValidator.naturalTextValidator(),Validators.required, Validators.minLength(1), Validators.maxLength(250)],
      }),
      minSalary: new FormControl(bandRawValue.minSalary, {
        validators: [Validators.required],
      }),
      maxSalary: new FormControl(bandRawValue.maxSalary, {
        validators: [Validators.required],
      }),
      welfareFund: new FormControl(bandRawValue.welfareFund, {
        validators: [Validators.min(0), Validators.max(100000)],
      }),
      mobileCelling: new FormControl(bandRawValue.mobileCelling, {
        validators: [Validators.min(0), Validators.max(100000)],
      }),
      createdAt: new FormControl(bandRawValue.createdAt),
      updatedAt: new FormControl(bandRawValue.updatedAt),
      createdBy: new FormControl(bandRawValue.createdBy),
      updatedBy: new FormControl(bandRawValue.updatedBy),
    });
  }

  getBand(form: BandFormGroup): IBand | NewBand {
    return form.getRawValue() as IBand | NewBand;
  }

  resetForm(form: BandFormGroup, band: BandFormGroupInput): void {
    const bandRawValue = { ...this.getFormDefaults(), ...band };
    form.reset(
      {
        ...bandRawValue,
        id: { value: bandRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BandFormDefaults {
    return {
      id: null,
    };
  }
}
