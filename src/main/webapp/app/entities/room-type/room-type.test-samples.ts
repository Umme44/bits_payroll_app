import dayjs from 'dayjs/esm';

import { IRoomType, NewRoomType } from './room-type.model';

export const sampleWithRequiredData: IRoomType = {
  id: 57008,
  typeName: 'generate quantify',
};

export const sampleWithPartialData: IRoomType = {
  id: 64789,
  typeName: 'payment AGP Plastic',
  remarks: 'Sol Chair Soft',
};

export const sampleWithFullData: IRoomType = {
  id: 78114,
  typeName: 'multi-byte',
  remarks: 'fault-tolerant withdrawal',
  createdAt: dayjs('2022-06-04T18:27'),
  updatedAt: dayjs('2022-06-04T15:18'),
};

export const sampleWithNewData: NewRoomType = {
  typeName: 'Quality Towels',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
