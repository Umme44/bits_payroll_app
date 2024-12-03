import dayjs from 'dayjs/esm';

import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { Status } from 'app/entities/enumerations/status.model';

import { IMovementEntry, NewMovementEntry } from './movement-entry.model';

export const sampleWithRequiredData: IMovementEntry = {
  id: 1983,
  startDate: dayjs('2022-01-27'),
  startTime: dayjs('2022-01-26T12:03'),
  startNote: 'bypassing',
  endDate: dayjs('2022-01-26'),
  endTime: dayjs('2022-01-26T23:47'),
  endNote: 'Agent Pants',
  type: MovementType['MOVEMENT'],
  status: Status['APPROVED'],
  createdAt: dayjs('2022-01-27'),
};

export const sampleWithPartialData: IMovementEntry = {
  id: 94347,
  startDate: dayjs('2022-01-26'),
  startTime: dayjs('2022-01-26T18:34'),
  startNote: 'Dollar',
  endDate: dayjs('2022-01-26'),
  endTime: dayjs('2022-01-27T07:03'),
  endNote: 'workforce Row',
  type: MovementType['MOVEMENT'],
  status: Status['PENDING'],
  createdAt: dayjs('2022-01-27'),
  sanctionAt: dayjs('2022-01-26'),
  note: 'Hat',
};

export const sampleWithFullData: IMovementEntry = {
  id: 1874,
  startDate: dayjs('2022-01-26'),
  startTime: dayjs('2022-01-26T19:22'),
  startNote: 'turquoise',
  endDate: dayjs('2022-01-26'),
  endTime: dayjs('2022-01-26T19:45'),
  endNote: 'teal integrated Solutions',
  type: MovementType['MOVEMENT'],
  status: Status['NOT_APPROVED'],
  createdAt: dayjs('2022-01-26'),
  updatedAt: dayjs('2022-01-26'),
  sanctionAt: dayjs('2022-01-27'),
  note: 'programming',
};

export const sampleWithNewData: NewMovementEntry = {
  startDate: dayjs('2022-01-26'),
  startTime: dayjs('2022-01-26T11:55'),
  startNote: 'array',
  endDate: dayjs('2022-01-26'),
  endTime: dayjs('2022-01-26T09:42'),
  endNote: 'Quetzal Borders Dominica',
  type: MovementType['MOVEMENT'],
  status: Status['PENDING'],
  createdAt: dayjs('2022-01-26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
