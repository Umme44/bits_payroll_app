import dayjs from 'dayjs/esm';

import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

import { IEmployeePinConfiguration, NewEmployeePinConfiguration } from './employee-pin-configuration.model';

export const sampleWithRequiredData: IEmployeePinConfiguration = {
  id: 2447,
  employeeCategory: EmployeeCategory['PART_TIME'],
  sequenceStart: 'Administrator',
  sequenceEnd: 'navigating',
  createdAt: dayjs('2022-10-30T12:06'),
};

export const sampleWithPartialData: IEmployeePinConfiguration = {
  id: 7213,
  employeeCategory: EmployeeCategory['INTERN'],
  sequenceStart: 'Frozen withdrawal actuating',
  sequenceEnd: 'Cheese Assurance',
  createdAt: dayjs('2022-10-30T11:02'),
  lastCreatedPin: 'navigate Gorgeous online',
};

export const sampleWithFullData: IEmployeePinConfiguration = {
  id: 68279,
  employeeCategory: EmployeeCategory['REGULAR_CONFIRMED_EMPLOYEE'],
  sequenceStart: 'input utilisation Specialist',
  sequenceEnd: 'PCI Handmade Hawaii',
  lastSequence: 'Cambridgeshire orchid',
  hasFullFilled: false,
  createdAt: dayjs('2022-10-31T02:08'),
  updatedAt: dayjs('2022-10-31T04:01'),
  lastCreatedPin: 'Borders',
};

export const sampleWithNewData: NewEmployeePinConfiguration = {
  employeeCategory: EmployeeCategory['REGULAR_PROVISIONAL_EMPLOYEE'],
  sequenceStart: 'backing Granite',
  sequenceEnd: 'strategy',
  createdAt: dayjs('2022-10-30T17:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
