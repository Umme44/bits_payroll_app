import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IItemInformation, NewItemInformation } from '../item-information.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItemInformation for edit and NewItemInformationFormGroupInput for create.
 */
type ItemInformationFormGroupInput = IItemInformation | PartialWithRequiredKeyOf<NewItemInformation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IItemInformation | NewItemInformation> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type ItemInformationFormRawValue = FormValueOf<IItemInformation>;

type NewItemInformationFormRawValue = FormValueOf<NewItemInformation>;

type ItemInformationFormDefaults = Pick<NewItemInformation, 'id' | 'createdAt' | 'updatedAt'>;

type ItemInformationFormGroupContent = {
  id: FormControl<ItemInformationFormRawValue['id'] | NewItemInformation['id']>;
  code: FormControl<ItemInformationFormRawValue['code']>;
  name: FormControl<ItemInformationFormRawValue['name']>;
  specification: FormControl<ItemInformationFormRawValue['specification']>;
  createdAt: FormControl<ItemInformationFormRawValue['createdAt']>;
  updatedAt: FormControl<ItemInformationFormRawValue['updatedAt']>;
  departmentId: FormControl<ItemInformationFormRawValue['departmentId']>;
  unitOfMeasurementId: FormControl<ItemInformationFormRawValue['unitOfMeasurementId']>;
  createdById: FormControl<ItemInformationFormRawValue['createdById']>;
  updatedById: FormControl<ItemInformationFormRawValue['updatedById']>;
};

export type ItemInformationFormGroup = FormGroup<ItemInformationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemInformationFormService {
  createItemInformationFormGroup(itemInformation: ItemInformationFormGroupInput = { id: null }): ItemInformationFormGroup {
    const itemInformationRawValue = this.convertItemInformationToItemInformationRawValue({
      ...this.getFormDefaults(),
      ...itemInformation,
    });
    return new FormGroup<ItemInformationFormGroupContent>({
      id: new FormControl(
        { value: itemInformationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(itemInformationRawValue.code),
      name: new FormControl(itemInformationRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      specification: new FormControl(itemInformationRawValue.specification, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(itemInformationRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(itemInformationRawValue.updatedAt),
      departmentId: new FormControl(itemInformationRawValue.departmentId, {
        validators: [Validators.required],
      }),
      unitOfMeasurementId: new FormControl(itemInformationRawValue.unitOfMeasurementId, {
        validators: [Validators.required],
      }),
      createdById: new FormControl(itemInformationRawValue.createdById),
      updatedById: new FormControl(itemInformationRawValue.updatedById),
    });
  }

  getItemInformation(form: ItemInformationFormGroup): IItemInformation | NewItemInformation {
    return this.convertItemInformationRawValueToItemInformation(
      form.getRawValue() as ItemInformationFormRawValue | NewItemInformationFormRawValue
    );
  }

  resetForm(form: ItemInformationFormGroup, itemInformation: ItemInformationFormGroupInput): void {
    const itemInformationRawValue = this.convertItemInformationToItemInformationRawValue({ ...this.getFormDefaults(), ...itemInformation });
    form.reset(
      {
        ...itemInformationRawValue,
        id: { value: itemInformationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ItemInformationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertItemInformationRawValueToItemInformation(
    rawItemInformation: ItemInformationFormRawValue | NewItemInformationFormRawValue
  ): IItemInformation | NewItemInformation {
    return {
      ...rawItemInformation,
      createdAt: dayjs(rawItemInformation.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawItemInformation.updatedAt, DATE_TIME_FORMAT),
      code: rawItemInformation.code ? rawItemInformation.code : ' ',
    };
  }

  private convertItemInformationToItemInformationRawValue(
    itemInformation: IItemInformation | (Partial<NewItemInformation> & ItemInformationFormDefaults)
  ): ItemInformationFormRawValue | PartialWithRequiredKeyOf<NewItemInformationFormRawValue> {
    return {
      ...itemInformation,
      createdAt: itemInformation.createdAt ? itemInformation.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: itemInformation.updatedAt ? itemInformation.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
