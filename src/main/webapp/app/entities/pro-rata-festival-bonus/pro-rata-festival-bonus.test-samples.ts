import dayjs from 'dayjs/esm';

import { IProRataFestivalBonus, NewProRataFestivalBonus } from './pro-rata-festival-bonus.model';

export const sampleWithRequiredData: IProRataFestivalBonus = {
  id: 80099,
};

export const sampleWithPartialData: IProRataFestivalBonus = {
  id: 62195,
};

export const sampleWithFullData: IProRataFestivalBonus = {
  id: 36454,
  date: dayjs('2021-08-22'),
  amount: 11543,
  description: 'yellow Louisiana',
};

export const sampleWithNewData: NewProRataFestivalBonus = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
