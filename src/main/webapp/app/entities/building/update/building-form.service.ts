import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBuilding, NewBuilding } from '../building.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBuilding for edit and NewBuildingFormGroupInput for create.
 */
type BuildingFormGroupInput = IBuilding | PartialWithRequiredKeyOf<NewBuilding>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBuilding | NewBuilding> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type BuildingFormRawValue = FormValueOf<IBuilding>;

type NewBuildingFormRawValue = FormValueOf<NewBuilding>;

type BuildingFormDefaults = Pick<NewBuilding, 'id' | 'createdAt' | 'updatedAt'>;

type BuildingFormGroupContent = {
  id: FormControl<BuildingFormRawValue['id'] | NewBuilding['id']>;
  buildingName: FormControl<BuildingFormRawValue['buildingName']>;
  buildingLocation: FormControl<BuildingFormRawValue['buildingLocation']>;
  remarks: FormControl<BuildingFormRawValue['remarks']>;
  createdAt: FormControl<BuildingFormRawValue['createdAt']>;
  updatedAt: FormControl<BuildingFormRawValue['updatedAt']>;
  createdBy: FormControl<BuildingFormRawValue['createdBy']>;
  updatedBy: FormControl<BuildingFormRawValue['updatedBy']>;
};

export type BuildingFormGroup = FormGroup<BuildingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BuildingFormService {
  createBuildingFormGroup(building: BuildingFormGroupInput = { id: null }): BuildingFormGroup {
    const buildingRawValue = this.convertBuildingToBuildingRawValue({
      ...this.getFormDefaults(),
      ...building,
    });
    return new FormGroup<BuildingFormGroupContent>({
      id: new FormControl(
        { value: buildingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      buildingName: new FormControl(buildingRawValue.buildingName, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250)],
      }),
      buildingLocation: new FormControl(buildingRawValue.buildingLocation, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      remarks: new FormControl(buildingRawValue.remarks, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      createdAt: new FormControl(buildingRawValue.createdAt),
      updatedAt: new FormControl(buildingRawValue.updatedAt),
      createdBy: new FormControl(buildingRawValue.createdBy),
      updatedBy: new FormControl(buildingRawValue.updatedBy),
    });
  }

  getBuilding(form: BuildingFormGroup): IBuilding | NewBuilding {
    return this.convertBuildingRawValueToBuilding(form.getRawValue() as BuildingFormRawValue | NewBuildingFormRawValue);
  }

  resetForm(form: BuildingFormGroup, building: BuildingFormGroupInput): void {
    const buildingRawValue = this.convertBuildingToBuildingRawValue({ ...this.getFormDefaults(), ...building });
    form.reset(
      {
        ...buildingRawValue,
        id: { value: buildingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BuildingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertBuildingRawValueToBuilding(rawBuilding: BuildingFormRawValue | NewBuildingFormRawValue): IBuilding | NewBuilding {
    return {
      ...rawBuilding,
      createdAt: dayjs(rawBuilding.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawBuilding.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertBuildingToBuildingRawValue(
    building: IBuilding | (Partial<NewBuilding> & BuildingFormDefaults)
  ): BuildingFormRawValue | PartialWithRequiredKeyOf<NewBuildingFormRawValue> {
    return {
      ...building,
      createdAt: building.createdAt ? building.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: building.updatedAt ? building.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
