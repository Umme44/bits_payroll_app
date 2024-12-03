import dayjs from 'dayjs/esm';

import { IFloor, NewFloor } from './floor.model';

export const sampleWithRequiredData: IFloor = {
  id: 27504,
  floorName: 'firewall',
};

export const sampleWithPartialData: IFloor = {
  id: 76161,
  floorName: 'Dynamic connect',
  createdAt: dayjs('2022-06-05T19:25'),
};

export const sampleWithFullData: IFloor = {
  id: 48697,
  floorName: 'Shoes SDD USB',
  remarks: 'base',
  createdAt: dayjs('2022-06-05T14:47'),
  updatedAt: dayjs('2022-06-06T10:09'),
};

export const sampleWithNewData: NewFloor = {
  floorName: 'Synergistic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
