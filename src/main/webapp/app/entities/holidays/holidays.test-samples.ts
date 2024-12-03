import dayjs from 'dayjs/esm';

import { HolidayType } from 'app/entities/enumerations/holiday-type.model';

import { IHolidays, NewHolidays } from './holidays.model';

export const sampleWithRequiredData: IHolidays = {
  id: 61471,
};

export const sampleWithPartialData: IHolidays = {
  id: 17138,
  endDate: dayjs('2021-03-01'),
};

export const sampleWithFullData: IHolidays = {
  id: 43411,
  holidayType: HolidayType['Weekly'],
  description: 'Franc bus uniform',
  startDate: dayjs('2021-02-28'),
  endDate: dayjs('2021-02-28'),
  isMoonDependent: false,
};

export const sampleWithNewData: NewHolidays = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
