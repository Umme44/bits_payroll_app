import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVehicleRequisition, NewVehicleRequisition } from '../vehicle-requisition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleRequisition for edit and NewVehicleRequisitionFormGroupInput for create.
 */
type VehicleRequisitionFormGroupInput = IVehicleRequisition | PartialWithRequiredKeyOf<NewVehicleRequisition>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVehicleRequisition | NewVehicleRequisition> = Omit<T, 'createdAt' | 'updatedAt' | 'sanctionAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionAt?: string | null;
};

type VehicleRequisitionFormRawValue = FormValueOf<IVehicleRequisition>;

type NewVehicleRequisitionFormRawValue = FormValueOf<NewVehicleRequisition>;

type VehicleRequisitionFormDefaults = Pick<NewVehicleRequisition, 'id' | 'createdAt' | 'updatedAt' | 'sanctionAt'>;

type VehicleRequisitionFormGroupContent = {
  id: FormControl<VehicleRequisitionFormRawValue['id'] | NewVehicleRequisition['id']>;
  purpose: FormControl<VehicleRequisitionFormRawValue['purpose']>;
  otherPassengersName: FormControl<VehicleRequisitionFormRawValue['otherPassengersName']>;
  totalNumberOfPassengers: FormControl<VehicleRequisitionFormRawValue['totalNumberOfPassengers']>;
  status: FormControl<VehicleRequisitionFormRawValue['status']>;
  remarks: FormControl<VehicleRequisitionFormRawValue['remarks']>;
  createdAt: FormControl<VehicleRequisitionFormRawValue['createdAt']>;
  updatedAt: FormControl<VehicleRequisitionFormRawValue['updatedAt']>;
  sanctionAt: FormControl<VehicleRequisitionFormRawValue['sanctionAt']>;
  transactionNumber: FormControl<VehicleRequisitionFormRawValue['transactionNumber']>;
  startDate: FormControl<VehicleRequisitionFormRawValue['startDate']>;
  endDate: FormControl<VehicleRequisitionFormRawValue['endDate']>;
  startTime: FormControl<VehicleRequisitionFormRawValue['startTime']>;
  endTime: FormControl<VehicleRequisitionFormRawValue['endTime']>;
  area: FormControl<VehicleRequisitionFormRawValue['area']>;
  createdBy: FormControl<VehicleRequisitionFormRawValue['createdBy']>;
  updatedBy: FormControl<VehicleRequisitionFormRawValue['updatedBy']>;
  approvedBy: FormControl<VehicleRequisitionFormRawValue['approvedBy']>;
  requester: FormControl<VehicleRequisitionFormRawValue['requester']>;
  vehicle: FormControl<VehicleRequisitionFormRawValue['vehicle']>;
};

export type VehicleRequisitionFormGroup = FormGroup<VehicleRequisitionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleRequisitionFormService {
  createVehicleRequisitionFormGroup(vehicleRequisition: VehicleRequisitionFormGroupInput = { id: null }): VehicleRequisitionFormGroup {
    const vehicleRequisitionRawValue = this.convertVehicleRequisitionToVehicleRequisitionRawValue({
      ...this.getFormDefaults(),
      ...vehicleRequisition,
    });
    return new FormGroup<VehicleRequisitionFormGroupContent>({
      id: new FormControl(
        { value: vehicleRequisitionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      purpose: new FormControl(vehicleRequisitionRawValue.purpose, {
        validators: [Validators.required],
      }),
      otherPassengersName: new FormControl(vehicleRequisitionRawValue.otherPassengersName),
      totalNumberOfPassengers: new FormControl(vehicleRequisitionRawValue.totalNumberOfPassengers, {
        validators: [Validators.required, Validators.min(0), Validators.max(500)],
      }),
      status: new FormControl(vehicleRequisitionRawValue.status, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(vehicleRequisitionRawValue.remarks),
      createdAt: new FormControl(vehicleRequisitionRawValue.createdAt),
      updatedAt: new FormControl(vehicleRequisitionRawValue.updatedAt),
      sanctionAt: new FormControl(vehicleRequisitionRawValue.sanctionAt),
      transactionNumber: new FormControl(vehicleRequisitionRawValue.transactionNumber),
      startDate: new FormControl(vehicleRequisitionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(vehicleRequisitionRawValue.endDate, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(vehicleRequisitionRawValue.startTime, {
        validators: [Validators.required, Validators.min(0), Validators.max(24)],
      }),
      endTime: new FormControl(vehicleRequisitionRawValue.endTime, {
        validators: [Validators.required, Validators.min(0), Validators.max(24)],
      }),
      area: new FormControl(vehicleRequisitionRawValue.area, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(vehicleRequisitionRawValue.createdBy),
      updatedBy: new FormControl(vehicleRequisitionRawValue.updatedBy),
      approvedBy: new FormControl(vehicleRequisitionRawValue.approvedBy),
      requester: new FormControl(vehicleRequisitionRawValue.requester),
      vehicle: new FormControl(vehicleRequisitionRawValue.vehicle),
    });
  }

  getVehicleRequisition(form: VehicleRequisitionFormGroup): IVehicleRequisition | NewVehicleRequisition {
    return this.convertVehicleRequisitionRawValueToVehicleRequisition(
      form.getRawValue() as VehicleRequisitionFormRawValue | NewVehicleRequisitionFormRawValue
    );
  }

  resetForm(form: VehicleRequisitionFormGroup, vehicleRequisition: VehicleRequisitionFormGroupInput): void {
    const vehicleRequisitionRawValue = this.convertVehicleRequisitionToVehicleRequisitionRawValue({
      ...this.getFormDefaults(),
      ...vehicleRequisition,
    });
    form.reset(
      {
        ...vehicleRequisitionRawValue,
        id: { value: vehicleRequisitionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VehicleRequisitionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      sanctionAt: currentTime,
    };
  }

  private convertVehicleRequisitionRawValueToVehicleRequisition(
    rawVehicleRequisition: VehicleRequisitionFormRawValue | NewVehicleRequisitionFormRawValue
  ): IVehicleRequisition | NewVehicleRequisition {
    return {
      ...rawVehicleRequisition,
      createdAt: dayjs(rawVehicleRequisition.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawVehicleRequisition.updatedAt, DATE_TIME_FORMAT),
      sanctionAt: dayjs(rawVehicleRequisition.sanctionAt, DATE_TIME_FORMAT),
    };
  }

  private convertVehicleRequisitionToVehicleRequisitionRawValue(
    vehicleRequisition: IVehicleRequisition | (Partial<NewVehicleRequisition> & VehicleRequisitionFormDefaults)
  ): VehicleRequisitionFormRawValue | PartialWithRequiredKeyOf<NewVehicleRequisitionFormRawValue> {
    return {
      ...vehicleRequisition,
      createdAt: vehicleRequisition.createdAt ? vehicleRequisition.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: vehicleRequisition.updatedAt ? vehicleRequisition.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      sanctionAt: vehicleRequisition.sanctionAt ? vehicleRequisition.sanctionAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
