import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

import { IFestivalBonusConfig, NewFestivalBonusConfig } from './festival-bonus-config.model';

export const sampleWithRequiredData: IFestivalBonusConfig = {
  id: 52375,
  employeeCategory: EmployeeCategory['CONSULTANTS'],
  percentageFromGross: 2343,
};

export const sampleWithPartialData: IFestivalBonusConfig = {
  id: 9480,
  employeeCategory: EmployeeCategory['INTERN'],
  percentageFromGross: 2665,
};

export const sampleWithFullData: IFestivalBonusConfig = {
  id: 39371,
  employeeCategory: EmployeeCategory['REGULAR_PROVISIONAL_EMPLOYEE'],
  percentageFromGross: 4389,
};

export const sampleWithNewData: NewFestivalBonusConfig = {
  employeeCategory: EmployeeCategory['PART_TIME'],
  percentageFromGross: 3989,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
