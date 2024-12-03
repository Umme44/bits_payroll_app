import dayjs from 'dayjs/esm';

import { IAitConfig, NewAitConfig } from './ait-config.model';

export const sampleWithRequiredData: IAitConfig = {
  id: 4914,
  startDate: dayjs('2021-04-27'),
  endDate: dayjs('2021-04-27'),
};

export const sampleWithPartialData: IAitConfig = {
  id: 42288,
  startDate: dayjs('2021-04-28'),
  endDate: dayjs('2021-04-27'),
};

export const sampleWithFullData: IAitConfig = {
  id: 11314,
  startDate: dayjs('2021-04-27'),
  endDate: dayjs('2021-04-27'),
  taxConfig: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAitConfig = {
  startDate: dayjs('2021-04-28'),
  endDate: dayjs('2021-04-28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
