import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFestival, NewFestival } from '../festival.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFestival for edit and NewFestivalFormGroupInput for create.
 */
type FestivalFormGroupInput = IFestival | PartialWithRequiredKeyOf<NewFestival>;

type FestivalFormDefaults = Pick<NewFestival, 'id' | 'isProRata'>;

type FestivalFormGroupContent = {
  id: FormControl<IFestival['id'] | NewFestival['id']>;
  title: FormControl<IFestival['title']>;
  festivalName: FormControl<IFestival['festivalName']>;
  festivalDate: FormControl<IFestival['festivalDate']>;
  bonusDisbursementDate: FormControl<IFestival['bonusDisbursementDate']>;
  religion: FormControl<IFestival['religion']>;
  isProRata: FormControl<IFestival['isProRata']>;
};

export type FestivalFormGroup = FormGroup<FestivalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FestivalFormService {
  createFestivalFormGroup(festival: FestivalFormGroupInput = { id: null }): FestivalFormGroup {
    const festivalRawValue = {
      ...this.getFormDefaults(),
      ...festival,
    };
    return new FormGroup<FestivalFormGroupContent>({
      id: new FormControl(
        { value: festivalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(festivalRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      festivalName: new FormControl(festivalRawValue.festivalName, {
        validators: [Validators.minLength(0), Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      festivalDate: new FormControl(festivalRawValue.festivalDate),
      bonusDisbursementDate: new FormControl(festivalRawValue.bonusDisbursementDate, {
        validators: [Validators.required],
      }),
      religion: new FormControl(festivalRawValue.religion, {
        validators: [Validators.required],
      }),
      isProRata: new FormControl(festivalRawValue.isProRata, {
        validators: [Validators.required],
      }),
    });
  }

  getFestival(form: FestivalFormGroup): IFestival | NewFestival {
    return form.getRawValue() as IFestival | NewFestival;
  }

  resetForm(form: FestivalFormGroup, festival: FestivalFormGroupInput): void {
    const festivalRawValue = { ...this.getFormDefaults(), ...festival };
    form.reset(
      {
        ...festivalRawValue,
        id: { value: festivalRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FestivalFormDefaults {
    return {
      id: null,
      isProRata: false,
    };
  }
}
