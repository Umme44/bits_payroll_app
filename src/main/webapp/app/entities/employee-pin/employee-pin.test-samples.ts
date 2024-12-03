import dayjs from 'dayjs/esm';

import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { EmployeePinStatus } from 'app/entities/enumerations/employee-pin-status.model';

import { IEmployeePin, NewEmployeePin } from './employee-pin.model';

export const sampleWithRequiredData: IEmployeePin = {
  id: 77391,
  fullName: 'plug-and-play Towels',
  employeeCategory: EmployeeCategory['INTERN'],
  employeePinStatus: EmployeePinStatus['CONTRACT_END'],
  createdAt: dayjs('2022-08-31T00:37'),
};

export const sampleWithPartialData: IEmployeePin = {
  id: 88309,
  fullName: 'Dynamic Plastic generate',
  employeeCategory: EmployeeCategory['CONSULTANTS'],
  employeePinStatus: EmployeePinStatus['NOT_JOINED'],
  createdAt: dayjs('2022-08-31T10:00'),
  updatedAt: dayjs('2022-08-31T06:11'),
};

export const sampleWithFullData: IEmployeePin = {
  id: 48476,
  pin: 'function',
  fullName: 'Specialist',
  employeeCategory: EmployeeCategory['PART_TIME'],
  employeePinStatus: EmployeePinStatus['CREATED'],
  createdAt: dayjs('2022-08-30T21:59'),
  updatedAt: dayjs('2022-08-31T04:32'),
};

export const sampleWithNewData: NewEmployeePin = {
  fullName: 'Missouri',
  employeeCategory: EmployeeCategory['CONSULTANTS'],
  employeePinStatus: EmployeePinStatus['RESIGNED'],
  createdAt: dayjs('2022-08-31T11:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
