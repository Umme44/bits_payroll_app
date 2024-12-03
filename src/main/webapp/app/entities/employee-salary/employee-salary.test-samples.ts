import dayjs from 'dayjs/esm';

import { Month } from 'app/entities/enumerations/month.model';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

import { IEmployeeSalary, NewEmployeeSalary } from './employee-salary.model';

export const sampleWithRequiredData: IEmployeeSalary = {
  id: 11420,
};

export const sampleWithPartialData: IEmployeeSalary = {
  id: 91429,
  year: 81871,
  salaryGenerationDate: dayjs('2021-02-17'),
  updatedBy: 'Expanded Cambridgeshire Grocery',
  joiningDate: dayjs('2021-02-17'),
  unit: 'Towels',
  mainGrossSalary: 13898,
  mainGrossBasicSalary: 38142,
  mainGrossHouseRent: 42067,
  fractionDays: 40707,
  payableGrossMedicalAllowance: 62971,
  welfareFundDeduction: 63509,
  otherDeduction: 53100,
  totalDeduction: 46403,
  gfContribution: 72955,
  provisionForLeaveEncashment: 68917,
  entertainment: 9106613,
  salaryAdjustment: -7415923,
  providentFundArrear: 7135106,
  allowance01: 1518611,
  allowance03: 5907820,
  allowance04: 720152,
  allowance05: 9494344,
  isHold: false,
  attendanceRegularisationEndDate: dayjs('2021-02-17'),
  pfArrear: 59755,
};

export const sampleWithFullData: IEmployeeSalary = {
  id: 86413,
  month: Month['MARCH'],
  year: 43776,
  salaryGenerationDate: dayjs('2021-02-16'),
  createdBy: 'Ergonomic Outdoors index',
  createdAt: dayjs('2021-02-17T09:51'),
  updatedBy: 'withdrawal',
  updatedAt: dayjs('2021-02-16T17:04'),
  refPin: 'PCI',
  pin: 'Organized Loan payment',
  joiningDate: dayjs('2021-02-16'),
  confirmationDate: dayjs('2021-02-16'),
  employeeCategory: EmployeeCategory['CONTRACTUAL_EMPLOYEE'],
  unit: 'supply-chains Florida digital',
  department: 'copy XSS hack',
  mainGrossSalary: 57272,
  mainGrossBasicSalary: 45290,
  mainGrossHouseRent: 59308,
  mainGrossMedicalAllowance: 33816,
  mainGrossConveyanceAllowance: 41472,
  absentDays: 16634,
  fractionDays: 38470,
  payableGrossSalary: 21334,
  payableGrossBasicSalary: 86501,
  payableGrossHouseRent: 16557,
  payableGrossMedicalAllowance: 42079,
  payableGrossConveyanceAllowance: 78810,
  arrearSalary: 28332,
  pfDeduction: 1095,
  taxDeduction: 83584,
  welfareFundDeduction: 71364,
  mobileBillDeduction: 58042,
  otherDeduction: 66211,
  totalDeduction: 32229,
  netPay: 15575,
  remarks: 'upward-trending',
  pfContribution: 39263,
  gfContribution: 33661,
  provisionForFestivalBonus: 35904,
  provisionForLeaveEncashment: 94158,
  isFinalized: false,
  isDispatched: false,
  entertainment: 7499480,
  utility: 1108677,
  otherAddition: 92634,
  salaryAdjustment: 7297969,
  providentFundArrear: 9879682,
  allowance01: 5909434,
  allowance02: 6947792,
  allowance03: 6364891,
  allowance04: 4631175,
  allowance05: 9875802,
  allowance06: 3007551,
  provisionForProjectBonus: 8293817,
  isHold: false,
  attendanceRegularisationStartDate: dayjs('2021-02-17'),
  attendanceRegularisationEndDate: dayjs('2021-02-16'),
  title: 'transmitter',
  isVisibleToEmployee: true,
  pfArrear: 25373,
  taxCalculationSnapshot: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewEmployeeSalary = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
