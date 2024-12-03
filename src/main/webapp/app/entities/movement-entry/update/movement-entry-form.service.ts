import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMovementEntry, NewMovementEntry } from '../movement-entry.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMovementEntry for edit and NewMovementEntryFormGroupInput for create.
 */
type MovementEntryFormGroupInput = IMovementEntry | PartialWithRequiredKeyOf<NewMovementEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMovementEntry | NewMovementEntry> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type MovementEntryFormRawValue = FormValueOf<IMovementEntry>;

type NewMovementEntryFormRawValue = FormValueOf<NewMovementEntry>;

type MovementEntryFormDefaults = Pick<NewMovementEntry, 'id' | 'startTime' | 'endTime'>;

type MovementEntryFormGroupContent = {
  id: FormControl<MovementEntryFormRawValue['id'] | NewMovementEntry['id']>;
  startDate: FormControl<MovementEntryFormRawValue['startDate']>;
  startTime: FormControl<MovementEntryFormRawValue['startTime']>;
  startNote: FormControl<MovementEntryFormRawValue['startNote']>;
  endDate: FormControl<MovementEntryFormRawValue['endDate']>;
  endTime: FormControl<MovementEntryFormRawValue['endTime']>;
  endNote: FormControl<MovementEntryFormRawValue['endNote']>;
  type: FormControl<MovementEntryFormRawValue['type']>;
  status: FormControl<MovementEntryFormRawValue['status']>;
  createdAt: FormControl<MovementEntryFormRawValue['createdAt']>;
  updatedAt: FormControl<MovementEntryFormRawValue['updatedAt']>;
  sanctionAt: FormControl<MovementEntryFormRawValue['sanctionAt']>;
  note: FormControl<MovementEntryFormRawValue['note']>;
  employeeId: FormControl<MovementEntryFormRawValue['employeeId']>;
  createdByLogin: FormControl<MovementEntryFormRawValue['createdByLogin']>;
  createdById: FormControl<MovementEntryFormRawValue['createdById']>;
  updatedByLogin: FormControl<MovementEntryFormRawValue['updatedByLogin']>;
  sanctionByLogin: FormControl<MovementEntryFormRawValue['sanctionByLogin']>;
};

export type MovementEntryFormGroup = FormGroup<MovementEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MovementEntryFormService {
  createMovementEntryFormGroup(movementEntry: MovementEntryFormGroupInput = { id: null }): MovementEntryFormGroup {
    const movementEntryRawValue = this.convertMovementEntryToMovementEntryRawValue({
      ...this.getFormDefaults(),
      ...movementEntry,
    });
    return new FormGroup<MovementEntryFormGroupContent>({
      id: new FormControl(
        { value: movementEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(movementEntryRawValue.startDate, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(movementEntryRawValue.startTime, {
        validators: [Validators.required],
      }),
      startNote: new FormControl(movementEntryRawValue.startNote, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      endDate: new FormControl(movementEntryRawValue.endDate, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(movementEntryRawValue.endTime, {
        validators: [Validators.required],
      }),
      endNote: new FormControl(movementEntryRawValue.endNote, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      type: new FormControl(movementEntryRawValue.type, {
        validators: [Validators.required],
      }),
      status: new FormControl(movementEntryRawValue.status, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(movementEntryRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(movementEntryRawValue.updatedAt),
      sanctionAt: new FormControl(movementEntryRawValue.sanctionAt),
      note: new FormControl(movementEntryRawValue.note, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      employeeId: new FormControl(movementEntryRawValue.employeeId, {
        validators: [Validators.required],
      }),
      createdByLogin: new FormControl(movementEntryRawValue.createdByLogin),
      createdById: new FormControl(movementEntryRawValue.createdById),
      updatedByLogin: new FormControl(movementEntryRawValue.updatedByLogin),
      sanctionByLogin: new FormControl(movementEntryRawValue.sanctionByLogin),
    });
  }

  getMovementEntry(form: MovementEntryFormGroup): IMovementEntry | NewMovementEntry {
    return this.convertMovementEntryRawValueToMovementEntry(form.getRawValue() as MovementEntryFormRawValue | NewMovementEntryFormRawValue);
  }

  resetForm(form: MovementEntryFormGroup, movementEntry: MovementEntryFormGroupInput): void {
    const movementEntryRawValue = this.convertMovementEntryToMovementEntryRawValue({ ...this.getFormDefaults(), ...movementEntry });
    form.reset(
      {
        ...movementEntryRawValue,
        id: { value: movementEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MovementEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertMovementEntryRawValueToMovementEntry(
    rawMovementEntry: MovementEntryFormRawValue | NewMovementEntryFormRawValue
  ): IMovementEntry | NewMovementEntry {
    return {
      ...rawMovementEntry,
      startTime: dayjs(rawMovementEntry.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawMovementEntry.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertMovementEntryToMovementEntryRawValue(
    movementEntry: IMovementEntry | (Partial<NewMovementEntry> & MovementEntryFormDefaults)
  ): MovementEntryFormRawValue | PartialWithRequiredKeyOf<NewMovementEntryFormRawValue> {
    return {
      ...movementEntry,
      startTime: movementEntry.startTime ? movementEntry.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: movementEntry.endTime ? movementEntry.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
