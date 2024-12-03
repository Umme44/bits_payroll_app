import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrainingHistory, NewTrainingHistory } from '../training-history.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrainingHistory for edit and NewTrainingHistoryFormGroupInput for create.
 */
type TrainingHistoryFormGroupInput = ITrainingHistory | PartialWithRequiredKeyOf<NewTrainingHistory>;

type TrainingHistoryFormDefaults = Pick<NewTrainingHistory, 'id'>;

type TrainingHistoryFormGroupContent = {
  id: FormControl<ITrainingHistory['id'] | NewTrainingHistory['id']>;
  trainingName: FormControl<ITrainingHistory['trainingName']>;
  coordinatedBy: FormControl<ITrainingHistory['coordinatedBy']>;
  dateOfCompletion: FormControl<ITrainingHistory['dateOfCompletion']>;
  employeeId: FormControl<ITrainingHistory['employeeId']>;
};

export type TrainingHistoryFormGroup = FormGroup<TrainingHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrainingHistoryFormService {
  createTrainingHistoryFormGroup(trainingHistory: TrainingHistoryFormGroupInput = { id: null }): TrainingHistoryFormGroup {
    const trainingHistoryRawValue = {
      ...this.getFormDefaults(),
      ...trainingHistory,
    };
    return new FormGroup<TrainingHistoryFormGroupContent>({
      id: new FormControl(
        { value: trainingHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      trainingName: new FormControl(trainingHistoryRawValue.trainingName, {
        validators: [Validators.required, CustomValidator.naturalTextValidator()]
      }),
      coordinatedBy: new FormControl(trainingHistoryRawValue.coordinatedBy, {
        validators: [Validators.required, CustomValidator.naturalTextValidator()]
      }),
      dateOfCompletion: new FormControl(trainingHistoryRawValue.dateOfCompletion, {
        validators: [Validators.required]
      }),
      employeeId: new FormControl(trainingHistoryRawValue.employeeId, {
        validators: [Validators.required]
      }),
    });
  }

  getTrainingHistory(form: TrainingHistoryFormGroup): ITrainingHistory | NewTrainingHistory {
    return form.getRawValue() as ITrainingHistory | NewTrainingHistory;
  }

  resetForm(form: TrainingHistoryFormGroup, trainingHistory: TrainingHistoryFormGroupInput): void {
    const trainingHistoryRawValue = { ...this.getFormDefaults(), ...trainingHistory };
    form.reset(
      {
        ...trainingHistoryRawValue,
        id: { value: trainingHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrainingHistoryFormDefaults {
    return {
      id: null,
    };
  }
}
