import { IArrearSalaryMaster, NewArrearSalaryMaster } from './arrear-salary-master.model';

export const sampleWithRequiredData: IArrearSalaryMaster = {
  id: 44095,
  title: 'magnetic',
  isLocked: false,
  isDeleted: true,
};

export const sampleWithPartialData: IArrearSalaryMaster = {
  id: 72881,
  title: 'navigate',
  isLocked: false,
  isDeleted: false,
};

export const sampleWithFullData: IArrearSalaryMaster = {
  id: 1150,
  title: 'even-keeled indexing',
  isLocked: true,
  isDeleted: true,
};

export const sampleWithNewData: NewArrearSalaryMaster = {
  title: 'applications Buckinghamshire',
  isLocked: false,
  isDeleted: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
