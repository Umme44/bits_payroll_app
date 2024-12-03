import dayjs from 'dayjs/esm';

import { IIndividualArrearSalary, NewIndividualArrearSalary } from './individual-arrear-salary.model';

export const sampleWithRequiredData: IIndividualArrearSalary = {
  id: 36196,
};

export const sampleWithPartialData: IIndividualArrearSalary = {
  id: 46540,
  effectiveDate: dayjs('2022-03-12'),
  existingGross: 84779,
  newGross: 75587,
  increment: 65563,
  taxDeduction: 31551,
  netPay: 9624,
  title: 'Shirt Money',
  titleEffectiveFrom: 'reboot',
  arrearRemarks: 'Intranet Market',
  festivalBonus: 85659,
};

export const sampleWithFullData: IIndividualArrearSalary = {
  id: 96270,
  effectiveDate: dayjs('2022-03-12'),
  existingBand: 'impactful leverage Concrete',
  newBand: 'Representative navigating Gorgeous',
  existingGross: 23370,
  newGross: 24060,
  increment: 53580,
  arrearSalary: 51284,
  arrearPfDeduction: 65282,
  taxDeduction: 46089,
  netPay: 68663,
  pfContribution: 74702,
  title: 'Fish SQL',
  titleEffectiveFrom: 'project Cambridgeshire Granite',
  arrearRemarks: 'human-resource Car',
  festivalBonus: 57337,
};

export const sampleWithNewData: NewIndividualArrearSalary = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
