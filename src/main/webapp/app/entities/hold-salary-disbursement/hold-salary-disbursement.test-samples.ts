import dayjs from 'dayjs/esm';

import { IHoldSalaryDisbursement, NewHoldSalaryDisbursement } from './hold-salary-disbursement.model';

export const sampleWithRequiredData: IHoldSalaryDisbursement = {
  id: 32746,
  date: dayjs('2022-03-03'),
};

export const sampleWithPartialData: IHoldSalaryDisbursement = {
  id: 84005,
  date: dayjs('2022-03-02'),
};

export const sampleWithFullData: IHoldSalaryDisbursement = {
  id: 4237,
  date: dayjs('2022-03-03'),
};

export const sampleWithNewData: NewHoldSalaryDisbursement = {
  date: dayjs('2022-03-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
