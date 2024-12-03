import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOffer, NewOffer } from '../offer.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOffer for edit and NewOfferFormGroupInput for create.
 */
type OfferFormGroupInput = IOffer | PartialWithRequiredKeyOf<NewOffer>;

type OfferFormDefaults = Pick<NewOffer, 'id'>;

type OfferFormGroupContent = {
  id: FormControl<IOffer['id'] | NewOffer['id']>;
  title: FormControl<IOffer['title']>;
  description: FormControl<IOffer['description']>;
  imagePath: FormControl<IOffer['imagePath']>;
  createdAt: FormControl<IOffer['createdAt']>;
};

export type OfferFormGroup = FormGroup<OfferFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfferFormService {
  createOfferFormGroup(offer: OfferFormGroupInput = { id: null }): OfferFormGroup {
    const offerRawValue = {
      ...this.getFormDefaults(),
      ...offer,
    };
    return new FormGroup<OfferFormGroupContent>({
      id: new FormControl(
        { value: offerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(offerRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      description: new FormControl(offerRawValue.description, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      imagePath: new FormControl(offerRawValue.imagePath),
      createdAt: new FormControl(offerRawValue.createdAt),
    });
  }

  getOffer(form: OfferFormGroup): IOffer | NewOffer {
    return form.getRawValue() as IOffer | NewOffer;
  }

  resetForm(form: OfferFormGroup, offer: OfferFormGroupInput): void {
    const offerRawValue = { ...this.getFormDefaults(), ...offer };
    form.reset(
      {
        ...offerRawValue,
        id: { value: offerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OfferFormDefaults {
    return {
      id: null,
    };
  }
}
