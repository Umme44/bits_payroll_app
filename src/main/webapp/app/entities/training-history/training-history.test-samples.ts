import dayjs from 'dayjs/esm';

import { ITrainingHistory, NewTrainingHistory } from './training-history.model';

export const sampleWithRequiredData: ITrainingHistory = {
  id: 51614,
};

export const sampleWithPartialData: ITrainingHistory = {
  id: 98207,
};

export const sampleWithFullData: ITrainingHistory = {
  id: 57797,
  trainingName: 'back-end',
  coordinatedBy: 'Re-engineered',
  dateOfCompletion: dayjs('2021-02-14'),
};

export const sampleWithNewData: NewTrainingHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
