import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEventLog, NewEventLog } from '../event-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventLog for edit and NewEventLogFormGroupInput for create.
 */
type EventLogFormGroupInput = IEventLog | PartialWithRequiredKeyOf<NewEventLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEventLog | NewEventLog> = Omit<T, 'performedAt'> & {
  performedAt?: string | null;
};

type EventLogFormRawValue = FormValueOf<IEventLog>;

type NewEventLogFormRawValue = FormValueOf<NewEventLog>;

type EventLogFormDefaults = Pick<NewEventLog, 'id' | 'performedAt'>;

type EventLogFormGroupContent = {
  id: FormControl<EventLogFormRawValue['id'] | NewEventLog['id']>;
  title: FormControl<EventLogFormRawValue['title']>;
  requestMethod: FormControl<EventLogFormRawValue['requestMethod']>;
  performedAt: FormControl<EventLogFormRawValue['performedAt']>;
  data: FormControl<EventLogFormRawValue['data']>;
  entityName: FormControl<EventLogFormRawValue['entityName']>;
  performedBy: FormControl<EventLogFormRawValue['performedBy']>;
};

export type EventLogFormGroup = FormGroup<EventLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventLogFormService {
  createEventLogFormGroup(eventLog: EventLogFormGroupInput = { id: null }): EventLogFormGroup {
    const eventLogRawValue = this.convertEventLogToEventLogRawValue({
      ...this.getFormDefaults(),
      ...eventLog,
    });
    return new FormGroup<EventLogFormGroupContent>({
      id: new FormControl(
        { value: eventLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(eventLogRawValue.title, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      requestMethod: new FormControl(eventLogRawValue.requestMethod),
      performedAt: new FormControl(eventLogRawValue.performedAt, {
        validators: [Validators.required],
      }),
      data: new FormControl(eventLogRawValue.data),
      entityName: new FormControl(eventLogRawValue.entityName),
      performedBy: new FormControl(eventLogRawValue.performedBy, {
        validators: [Validators.required],
      }),
    });
  }

  getEventLog(form: EventLogFormGroup): IEventLog | NewEventLog {
    return this.convertEventLogRawValueToEventLog(form.getRawValue() as EventLogFormRawValue | NewEventLogFormRawValue);
  }

  resetForm(form: EventLogFormGroup, eventLog: EventLogFormGroupInput): void {
    const eventLogRawValue = this.convertEventLogToEventLogRawValue({ ...this.getFormDefaults(), ...eventLog });
    form.reset(
      {
        ...eventLogRawValue,
        id: { value: eventLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      performedAt: currentTime,
    };
  }

  private convertEventLogRawValueToEventLog(rawEventLog: EventLogFormRawValue | NewEventLogFormRawValue): IEventLog | NewEventLog {
    return {
      ...rawEventLog,
      performedAt: dayjs(rawEventLog.performedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEventLogToEventLogRawValue(
    eventLog: IEventLog | (Partial<NewEventLog> & EventLogFormDefaults)
  ): EventLogFormRawValue | PartialWithRequiredKeyOf<NewEventLogFormRawValue> {
    return {
      ...eventLog,
      performedAt: eventLog.performedAt ? eventLog.performedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
