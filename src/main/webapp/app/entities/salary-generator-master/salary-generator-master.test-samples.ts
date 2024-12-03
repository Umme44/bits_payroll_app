import { Visibility } from 'app/entities/enumerations/visibility.model';

import { ISalaryGeneratorMaster, NewSalaryGeneratorMaster } from './salary-generator-master.model';

export const sampleWithRequiredData: ISalaryGeneratorMaster = {
  id: 40873,
};

export const sampleWithPartialData: ISalaryGeneratorMaster = {
  id: 64978,
  year: 'Investment Representative fault-tolerant',
  isMobileBillImported: false,
  isSalaryDeductionImported: false,
  isFinalized: true,
  visibility: Visibility['VISIBLE_TO_EMPLOYEE'],
};

export const sampleWithFullData: ISalaryGeneratorMaster = {
  id: 59877,
  year: 'generating Optimization',
  month: 'Granite',
  isGenerated: true,
  isMobileBillImported: false,
  isPFLoanRepaymentImported: true,
  isAttendanceImported: true,
  isSalaryDeductionImported: false,
  isFinalized: false,
  visibility: Visibility['NOT_VISIBLE_TO_EMPLOYEE'],
};

export const sampleWithNewData: NewSalaryGeneratorMaster = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
