import dayjs from 'dayjs/esm';

import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 302,
  roomName: 'repurpose RSS',
  capacity: 4,
};

export const sampleWithPartialData: IRoom = {
  id: 59376,
  roomName: 'Unit Bypass Agent',
  remarks: 'Concrete Triple-buffered',
  capacity: 67,
  facilities: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IRoom = {
  id: 6875,
  roomName: 'Metical',
  remarks: 'Home',
  createdAt: dayjs('2022-06-06T23:50'),
  updatedAt: dayjs('2022-06-06T19:24'),
  capacity: 60,
  facilities: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRoom = {
  roomName: 'EXE',
  capacity: 73,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
