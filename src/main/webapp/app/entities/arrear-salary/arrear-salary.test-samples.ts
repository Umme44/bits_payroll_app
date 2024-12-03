import { Month } from 'app/entities/enumerations/month.model';

import { IArrearSalary, NewArrearSalary } from './arrear-salary.model';

export const sampleWithRequiredData: IArrearSalary = {
  id: 79462,
  month: Month['AUGUST'],
  year: 2020,
  amount: 9648712,
};

export const sampleWithPartialData: IArrearSalary = {
  id: 69690,
  month: Month['APRIL'],
  year: 1992,
  amount: 7315228,
  arrearType: 'hack Guarani',
};

export const sampleWithFullData: IArrearSalary = {
  id: 58888,
  month: Month['MAY'],
  year: 2045,
  amount: 5629553,
  arrearType: 'copying SDD',
};

export const sampleWithNewData: NewArrearSalary = {
  month: Month['OCTOBER'],
  year: 2033,
  amount: 9760599,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
