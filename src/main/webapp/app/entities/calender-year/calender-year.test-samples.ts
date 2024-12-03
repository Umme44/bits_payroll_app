import dayjs from 'dayjs/esm';

import { ICalenderYear, NewCalenderYear } from './calender-year.model';

export const sampleWithRequiredData: ICalenderYear = {
  id: 40304,
  year: 2039,
  startDate: dayjs('2021-05-21'),
  endDate: dayjs('2021-05-21'),
};

export const sampleWithPartialData: ICalenderYear = {
  id: 29314,
  year: 2070,
  startDate: dayjs('2021-05-21'),
  endDate: dayjs('2021-05-21'),
};

export const sampleWithFullData: ICalenderYear = {
  id: 24303,
  year: 2004,
  startDate: dayjs('2021-05-21'),
  endDate: dayjs('2021-05-21'),
};

export const sampleWithNewData: NewCalenderYear = {
  year: 1920,
  startDate: dayjs('2021-05-21'),
  endDate: dayjs('2021-05-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
