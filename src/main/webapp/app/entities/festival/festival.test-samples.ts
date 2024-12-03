import dayjs from 'dayjs/esm';

import { Religion } from 'app/entities/enumerations/religion.model';

import { IFestival, NewFestival } from './festival.model';

export const sampleWithRequiredData: IFestival = {
  id: 62870,
  title: 'Account Car salmon',
  bonusDisbursementDate: dayjs('2021-05-21'),
  religion: Religion['CHRISTIAN'],
  isProRata: true,
};

export const sampleWithPartialData: IFestival = {
  id: 67784,
  title: 'transition',
  bonusDisbursementDate: dayjs('2021-05-21'),
  religion: Religion['OTHER'],
  isProRata: false,
};

export const sampleWithFullData: IFestival = {
  id: 11067,
  title: 'protocol rich multi-byte',
  festivalName: 'hack',
  festivalDate: dayjs('2021-05-21'),
  bonusDisbursementDate: dayjs('2021-05-21'),
  religion: Religion['ALL'],
  isProRata: true,
};

export const sampleWithNewData: NewFestival = {
  title: 'visionary Sahara',
  bonusDisbursementDate: dayjs('2021-05-21'),
  religion: Religion['BUDDHA'],
  isProRata: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
