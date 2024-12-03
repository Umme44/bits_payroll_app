import dayjs from 'dayjs/esm';

import { IEmployeeDocument, NewEmployeeDocument } from './employee-document.model';

export const sampleWithRequiredData: IEmployeeDocument = {
  id: 87029,
  pin: 'Centralized District grow',
  fileName: 'client-driven Regional',
  filePath: 'Soft Bypass withdrawal',
};

export const sampleWithPartialData: IEmployeeDocument = {
  id: 18907,
  pin: 'Card violet',
  fileName: 'Handcrafted',
  filePath: 'Networked',
  hasEmployeeVisibility: true,
  remarks: 'Account Marketing Investment',
  createdBy: 'Specialist',
  updatedBy: 'compressing multi-state transmitter',
  updatedAt: dayjs('2023-12-06T08:38'),
};

export const sampleWithFullData: IEmployeeDocument = {
  id: 51876,
  pin: 'Metal Ameliorated',
  fileName: 'Lodge Solutions',
  filePath: 'Fresh Small Illinois',
  hasEmployeeVisibility: true,
  remarks: 'Towels Bike',
  createdBy: 'Oregon analyzer Home',
  createdAt: dayjs('2023-12-06T20:29'),
  updatedBy: 'enterprise Corners transform',
  updatedAt: dayjs('2023-12-06T21:02'),
  fileExtension: 'virtual National',
};

export const sampleWithNewData: NewEmployeeDocument = {
  pin: 'orchestration',
  fileName: 'cross-platform withdrawal bricks-and-clicks',
  filePath: 'generate PCI withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
