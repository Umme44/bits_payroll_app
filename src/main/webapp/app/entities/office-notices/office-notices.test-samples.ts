import dayjs from 'dayjs/esm';

import { NoticeStatus } from 'app/entities/enumerations/notice-status.model';

import { IOfficeNotices, NewOfficeNotices } from './office-notices.model';

export const sampleWithRequiredData: IOfficeNotices = {
  id: 54647,
  title: 'ubiquitous Central rich',
};

export const sampleWithPartialData: IOfficeNotices = {
  id: 36410,
  title: 'Kwanza feed',
  description: '../fake-data/blob/hipster.txt',
  publishForm: dayjs('2021-07-08'),
  createdAt: dayjs('2021-07-07'),
  createdBy: 'Coordinator',
  updatedBy: 'capacity',
};

export const sampleWithFullData: IOfficeNotices = {
  id: 64931,
  title: 'Loan Coordinator Toys',
  description: '../fake-data/blob/hipster.txt',
  status: NoticeStatus['UNPUBLISHED'],
  publishForm: dayjs('2021-07-08'),
  publishTo: dayjs('2021-07-08'),
  createdAt: dayjs('2021-07-08'),
  updatedAt: dayjs('2021-07-07'),
  createdBy: 'Aruba Designer fuchsia',
  updatedBy: 'orchid De-engineered',
};

export const sampleWithNewData: NewOfficeNotices = {
  title: 'cross-platform',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
