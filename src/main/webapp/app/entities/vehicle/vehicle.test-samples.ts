import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 47420,
  modelName: 'invoice Sausages tan',
  chassisNumber: 'Jewelery',
  registrationNumber: 'visualize backing Operative',
  status: Status['APPROVED'],
  capacity: 66596,
};

export const sampleWithPartialData: IVehicle = {
  id: 15725,
  modelName: 'archive e-tailers Valleys',
  chassisNumber: 'Fresh',
  registrationNumber: 'Direct',
  status: Status['APPROVED'],
  capacity: 19534,
  remarks: 'black',
  approvedAt: dayjs('2022-06-11T16:46'),
};

export const sampleWithFullData: IVehicle = {
  id: 61870,
  modelName: 'green',
  chassisNumber: 'Persistent',
  registrationNumber: 'client-driven monitor Drive',
  status: Status['PENDING'],
  capacity: 95417,
  remarks: 'Toys',
  createdAt: dayjs('2022-06-12T10:20'),
  updatedAt: dayjs('2022-06-12T07:06'),
  approvedAt: dayjs('2022-06-12T09:33'),
};

export const sampleWithNewData: NewVehicle = {
  modelName: 'hierarchy',
  chassisNumber: 'Point',
  registrationNumber: 'Steel',
  status: Status['APPROVED'],
  capacity: 82367,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
