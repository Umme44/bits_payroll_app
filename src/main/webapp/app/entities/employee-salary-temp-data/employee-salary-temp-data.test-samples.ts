import { Month } from 'app/entities/enumerations/month.model';

import { IEmployeeSalaryTempData, NewEmployeeSalaryTempData } from './employee-salary-temp-data.model';

export const sampleWithRequiredData: IEmployeeSalaryTempData = {
  id: 21526,
};

export const sampleWithPartialData: IEmployeeSalaryTempData = {
  id: 35941,
  month: Month['JULY'],
  year: 19136,
  mainGrossSalary: 51480,
  mainGrossBasicSalary: 52566,
  mainGrossHouseRent: 27488,
  mainGrossMedicalAllowance: 29699,
  mainGrossConveyanceAllowance: 12238,
  absentDays: 54677,
  fractionDays: 48995,
  payableGrossBasicSalary: 38208,
  arrearSalary: 5043,
  pfDeduction: 87094,
  taxDeduction: 2013,
  otherDeduction: 79894,
  netPay: 23334,
  remarks: 'Refined Card Solutions',
  gfContribution: 54270,
  provisionForFestivalBonus: 93432,
  livingAllowance: 90889,
  otherAddition: 31313,
  entertainment: 87855,
  utility: 86184,
};

export const sampleWithFullData: IEmployeeSalaryTempData = {
  id: 13192,
  month: Month['JUNE'],
  year: 32094,
  mainGrossSalary: 90652,
  mainGrossBasicSalary: 97596,
  mainGrossHouseRent: 9629,
  mainGrossMedicalAllowance: 98010,
  mainGrossConveyanceAllowance: 48375,
  absentDays: 74711,
  fractionDays: 8535,
  payableGrossSalary: 970,
  payableGrossBasicSalary: 47824,
  payableGrossHouseRent: 26885,
  payableGrossMedicalAllowance: 47668,
  payableGrossConveyanceAllowance: 70210,
  arrearSalary: 48051,
  pfDeduction: 74123,
  taxDeduction: 5455,
  welfareFundDeduction: 14818,
  mobileBillDeduction: 32662,
  otherDeduction: 78247,
  totalDeduction: 28445,
  netPay: 40636,
  remarks: 'virtual',
  pfContribution: 34965,
  gfContribution: 66507,
  provisionForFestivalBonus: 60352,
  provisionForLeaveEncashment: 69720,
  provishionForProjectBonus: 1018,
  livingAllowance: 78354,
  otherAddition: 50653,
  salaryAdjustment: 54871,
  providentFundArrear: 39101,
  entertainment: 3498,
  utility: 55230,
};

export const sampleWithNewData: NewEmployeeSalaryTempData = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
