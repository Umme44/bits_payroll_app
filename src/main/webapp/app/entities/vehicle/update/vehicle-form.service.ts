import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVehicle, NewVehicle } from '../vehicle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicle for edit and NewVehicleFormGroupInput for create.
 */
type VehicleFormGroupInput = IVehicle | PartialWithRequiredKeyOf<NewVehicle>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVehicle | NewVehicle> = Omit<T, 'createdAt' | 'updatedAt' | 'approvedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  approvedAt?: string | null;
};

type VehicleFormRawValue = FormValueOf<IVehicle>;

type NewVehicleFormRawValue = FormValueOf<NewVehicle>;

type VehicleFormDefaults = Pick<NewVehicle, 'id' | 'createdAt' | 'updatedAt' | 'approvedAt'>;

type VehicleFormGroupContent = {
  id: FormControl<VehicleFormRawValue['id'] | NewVehicle['id']>;
  modelName: FormControl<VehicleFormRawValue['modelName']>;
  chassisNumber: FormControl<VehicleFormRawValue['chassisNumber']>;
  registrationNumber: FormControl<VehicleFormRawValue['registrationNumber']>;
  status: FormControl<VehicleFormRawValue['status']>;
  capacity: FormControl<VehicleFormRawValue['capacity']>;
  remarks: FormControl<VehicleFormRawValue['remarks']>;
  createdAt: FormControl<VehicleFormRawValue['createdAt']>;
  updatedAt: FormControl<VehicleFormRawValue['updatedAt']>;
  approvedAt: FormControl<VehicleFormRawValue['approvedAt']>;
  createdBy: FormControl<VehicleFormRawValue['createdBy']>;
  updatedBy: FormControl<VehicleFormRawValue['updatedBy']>;
  approvedBy: FormControl<VehicleFormRawValue['approvedBy']>;
  assignedDriver: FormControl<VehicleFormRawValue['assignedDriver']>;
};

export type VehicleFormGroup = FormGroup<VehicleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleFormService {
  createVehicleFormGroup(vehicle: VehicleFormGroupInput = { id: null }): VehicleFormGroup {
    const vehicleRawValue = this.convertVehicleToVehicleRawValue({
      ...this.getFormDefaults(),
      ...vehicle,
    });
    return new FormGroup<VehicleFormGroupContent>({
      id: new FormControl(
        { value: vehicleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      modelName: new FormControl(vehicleRawValue.modelName, {
        validators: [Validators.required],
      }),
      chassisNumber: new FormControl(vehicleRawValue.chassisNumber, {
        validators: [Validators.required],
      }),
      registrationNumber: new FormControl(vehicleRawValue.registrationNumber, {
        validators: [Validators.required],
      }),
      status: new FormControl(vehicleRawValue.status, {
        validators: [Validators.required],
      }),
      capacity: new FormControl(vehicleRawValue.capacity, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(vehicleRawValue.remarks),
      createdAt: new FormControl(vehicleRawValue.createdAt),
      updatedAt: new FormControl(vehicleRawValue.updatedAt),
      approvedAt: new FormControl(vehicleRawValue.approvedAt),
      createdBy: new FormControl(vehicleRawValue.createdBy),
      updatedBy: new FormControl(vehicleRawValue.updatedBy),
      approvedBy: new FormControl(vehicleRawValue.approvedBy),
      assignedDriver: new FormControl(vehicleRawValue.assignedDriver, {
        validators: [Validators.required],
      }),
    });
  }

  getVehicle(form: VehicleFormGroup): IVehicle | NewVehicle {
    return this.convertVehicleRawValueToVehicle(form.getRawValue() as VehicleFormRawValue | NewVehicleFormRawValue);
  }

  resetForm(form: VehicleFormGroup, vehicle: VehicleFormGroupInput): void {
    const vehicleRawValue = this.convertVehicleToVehicleRawValue({ ...this.getFormDefaults(), ...vehicle });
    form.reset(
      {
        ...vehicleRawValue,
        id: { value: vehicleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VehicleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      approvedAt: currentTime,
    };
  }

  private convertVehicleRawValueToVehicle(rawVehicle: VehicleFormRawValue | NewVehicleFormRawValue): IVehicle | NewVehicle {
    return {
      ...rawVehicle,
      createdAt: dayjs(rawVehicle.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawVehicle.updatedAt, DATE_TIME_FORMAT),
      approvedAt: dayjs(rawVehicle.approvedAt, DATE_TIME_FORMAT),
    };
  }

  private convertVehicleToVehicleRawValue(
    vehicle: IVehicle | (Partial<NewVehicle> & VehicleFormDefaults)
  ): VehicleFormRawValue | PartialWithRequiredKeyOf<NewVehicleFormRawValue> {
    return {
      ...vehicle,
      createdAt: vehicle.createdAt ? vehicle.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: vehicle.updatedAt ? vehicle.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      approvedAt: vehicle.approvedAt ? vehicle.approvedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
