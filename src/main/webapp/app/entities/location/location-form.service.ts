import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ILocation, NewLocation } from '../../shared/model/location.model';
import {CustomValidator} from "../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormDefaults = Pick<NewLocation, 'id'>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  locationType: FormControl<ILocation['locationType']>;
  locationName: FormControl<ILocation['locationName']>;
  createdAt: FormControl<ILocation['createdAt']>;
  updateAt: FormControl<ILocation['updateAt']>;
  locationCode: FormControl<ILocation['locationCode']>;
  parentId: FormControl<ILocation['parentId']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      locationType: new FormControl(locationRawValue.locationType, {
        validators: [Validators.required],
      }),
      locationName: new FormControl(locationRawValue.locationName, {
        validators: [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      createdAt: new FormControl(locationRawValue.createdAt),
      updateAt: new FormControl(locationRawValue.updateAt),
      locationCode: new FormControl(locationRawValue.locationCode, {
        validators: [Validators.required, Validators.maxLength(250), CustomValidator.alphaNumericValidator()],
      }),
      parentId: new FormControl(locationRawValue.parentId),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
    };
  }
}
