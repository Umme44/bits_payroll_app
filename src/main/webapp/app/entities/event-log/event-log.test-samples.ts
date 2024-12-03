import dayjs from 'dayjs/esm';

import { RequestMethod } from 'app/entities/enumerations/request-method.model';

import { IEventLog, NewEventLog } from './event-log.model';

export const sampleWithRequiredData: IEventLog = {
  id: 91848,
  performedAt: dayjs('2022-04-18T09:01'),
};

export const sampleWithPartialData: IEventLog = {
  id: 77057,
  title: 'Metal bypass',
  performedAt: dayjs('2022-04-19T04:29'),
  data: '../fake-data/blob/hipster.txt',
  entityName: 'cross-platform',
};

export const sampleWithFullData: IEventLog = {
  id: 82865,
  title: 'Cotton Personal Cotton',
  requestMethod: RequestMethod['DELETE'],
  performedAt: dayjs('2022-04-19T04:13'),
  data: '../fake-data/blob/hipster.txt',
  entityName: 'Profound',
};

export const sampleWithNewData: NewEventLog = {
  performedAt: dayjs('2022-04-19T05:46'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
