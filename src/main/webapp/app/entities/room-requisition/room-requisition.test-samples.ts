import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IRoomRequisition, NewRoomRequisition } from './room-requisition.model';

export const sampleWithRequiredData: IRoomRequisition = {
  id: 52580,
  status: Status['PENDING'],
  bookingStartDate: dayjs('2022-06-07'),
  bookingEndDate: dayjs('2022-06-08'),
  startTime: 31,
  endTime: 45,
  title: 'Innovative',
};

export const sampleWithPartialData: IRoomRequisition = {
  id: 87275,
  status: Status['NOT_APPROVED'],
  bookingTrn: 'Extensions plum',
  sanctionedAt: dayjs('2022-06-08T08:23'),
  bookingStartDate: dayjs('2022-06-08'),
  bookingEndDate: dayjs('2022-06-08'),
  startTime: 8,
  endTime: 54,
  title: 'Soft',
  optionalParticipantList: '../fake-data/blob/hipster.txt',
  isFullDay: false,
};

export const sampleWithFullData: IRoomRequisition = {
  id: 72874,
  status: Status['APPROVED'],
  bookingTrn: 'User-friendly',
  createdAt: dayjs('2022-06-07T16:06'),
  updatedAt: dayjs('2022-06-08T09:32'),
  sanctionedAt: dayjs('2022-06-08T03:11'),
  participantList: '../fake-data/blob/hipster.txt',
  rejectedReason: 'Car Sausages',
  bookingStartDate: dayjs('2022-06-08'),
  bookingEndDate: dayjs('2022-06-07'),
  startTime: 51,
  endTime: 30,
  title: 'Card Decentralized Forward',
  agenda: 'holistic',
  optionalParticipantList: '../fake-data/blob/hipster.txt',
  isFullDay: false,
};

export const sampleWithNewData: NewRoomRequisition = {
  status: Status['PENDING'],
  bookingStartDate: dayjs('2022-06-07'),
  bookingEndDate: dayjs('2022-06-07'),
  startTime: 63,
  endTime: 57,
  title: 'Applications',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
