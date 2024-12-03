import { ISalaryDeduction, NewSalaryDeduction } from './salary-deduction.model';

export const sampleWithRequiredData: ISalaryDeduction = {
  id: 96827,
  deductionAmount: 530103,
  deductionYear: 2007,
  deductionMonth: 7,
};

export const sampleWithPartialData: ISalaryDeduction = {
  id: 97214,
  deductionAmount: 776116,
  deductionYear: 2052,
  deductionMonth: 1,
};

export const sampleWithFullData: ISalaryDeduction = {
  id: 15358,
  deductionAmount: 882339,
  deductionYear: 2014,
  deductionMonth: 7,
};

export const sampleWithNewData: NewSalaryDeduction = {
  deductionAmount: 617880,
  deductionYear: 2055,
  deductionMonth: 7,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
