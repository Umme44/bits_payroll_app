import { Month } from 'app/entities/enumerations/month.model';

import { IPfArrear, NewPfArrear } from './pf-arrear.model';

export const sampleWithRequiredData: IPfArrear = {
  id: 64527,
  month: Month['JANUARY'],
  year: 1964,
  amount: 7018171,
  remarks: 'Harbor Berkshire Keyboard',
};

export const sampleWithPartialData: IPfArrear = {
  id: 89456,
  month: Month['SEPTEMBER'],
  year: 1924,
  amount: 8540536,
  remarks: 'synthesizing real-time',
};

export const sampleWithFullData: IPfArrear = {
  id: 48315,
  month: Month['DECEMBER'],
  year: 1912,
  amount: 289052,
  remarks: 'Public-key Interactions Cambridgeshire',
};

export const sampleWithNewData: NewPfArrear = {
  month: Month['JUNE'],
  year: 2001,
  amount: 827875,
  remarks: 'Tuna turn-key Extended',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
