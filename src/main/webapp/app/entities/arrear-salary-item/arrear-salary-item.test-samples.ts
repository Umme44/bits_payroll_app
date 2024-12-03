import { IArrearSalaryItem, NewArrearSalaryItem } from './arrear-salary-item.model';

export const sampleWithRequiredData: IArrearSalaryItem = {
  id: 15466,
  title: 'Account',
  arrearAmount: 6332620,
  isDeleted: false,
};

export const sampleWithPartialData: IArrearSalaryItem = {
  id: 96805,
  title: 'virtual',
  arrearAmount: 572074,
  hasPfArrearDeduction: false,
  isDeleted: false,
};

export const sampleWithFullData: IArrearSalaryItem = {
  id: 15550,
  title: 'Strategist overriding Plastic',
  arrearAmount: 5120288,
  hasPfArrearDeduction: true,
  pfArrearDeduction: 49285,
  isFestivalBonus: false,
  isDeleted: false,
};

export const sampleWithNewData: NewArrearSalaryItem = {
  title: 'benchmark Books Jordan',
  arrearAmount: 9277148,
  isDeleted: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
