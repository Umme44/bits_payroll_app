import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IVehicleRequisition, NewVehicleRequisition } from './vehicle-requisition.model';

export const sampleWithRequiredData: IVehicleRequisition = {
  id: 28728,
  purpose: 'whiteboard Dobra infrastructures',
  totalNumberOfPassengers: 227,
  status: Status['APPROVED'],
  startDate: dayjs('2022-06-12'),
  endDate: dayjs('2022-06-12'),
  startTime: 7,
  endTime: 22,
  area: 'index action-items',
};

export const sampleWithPartialData: IVehicleRequisition = {
  id: 92970,
  purpose: 'Bedfordshire Movies',
  otherPassengersName: 'withdrawal analyzing wireless',
  totalNumberOfPassengers: 485,
  status: Status['NOT_APPROVED'],
  createdAt: dayjs('2022-06-12T18:31'),
  updatedAt: dayjs('2022-06-12T08:58'),
  startDate: dayjs('2022-06-12'),
  endDate: dayjs('2022-06-12'),
  startTime: 14,
  endTime: 7,
  area: 'programming',
};

export const sampleWithFullData: IVehicleRequisition = {
  id: 48547,
  purpose: 'Niger',
  otherPassengersName: 'transmitting maximize',
  totalNumberOfPassengers: 351,
  status: Status['APPROVED'],
  remarks: 'National',
  createdAt: dayjs('2022-06-12T10:42'),
  updatedAt: dayjs('2022-06-12T07:41'),
  sanctionAt: dayjs('2022-06-12T08:13'),
  transactionNumber: 'Small',
  startDate: dayjs('2022-06-12'),
  endDate: dayjs('2022-06-13'),
  startTime: 8,
  endTime: 12,
  area: 'Islands, streamline Minnesota',
};

export const sampleWithNewData: NewVehicleRequisition = {
  purpose: 'Customer',
  totalNumberOfPassengers: 408,
  status: Status['NOT_APPROVED'],
  startDate: dayjs('2022-06-12'),
  endDate: dayjs('2022-06-12'),
  startTime: 9,
  endTime: 11,
  area: 'Account',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
