import dayjs from 'dayjs/esm';

import { InsuranceRelation } from 'app/entities/enumerations/insurance-relation.model';
import { InsuranceStatus } from 'app/entities/enumerations/insurance-status.model';

import { IInsuranceRegistration, NewInsuranceRegistration } from './insurance-registration.model';

export const sampleWithRequiredData: IInsuranceRegistration = {
  id: 20445,
  dateOfBirth: dayjs('2022-05-25'),
  insuranceRelation: InsuranceRelation['SPOUSE'],
  insuranceStatus: InsuranceStatus['SEPARATED'],
  availableBalance: 68283,
  createdAt: dayjs('2022-05-25T01:10'),
};

export const sampleWithPartialData: IInsuranceRegistration = {
  id: 86604,
  dateOfBirth: dayjs('2022-05-25'),
  insuranceRelation: InsuranceRelation['SPOUSE'],
  insuranceStatus: InsuranceStatus['PENDING'],
  availableBalance: 77711,
  createdAt: dayjs('2022-05-24T09:33'),
};

export const sampleWithFullData: IInsuranceRegistration = {
  id: 87954,
  name: 'Plastic Quality Concrete',
  dateOfBirth: dayjs('2022-05-24'),
  photo: '../fake-data/blob/hipster.txt',
  insuranceRelation: InsuranceRelation['SPOUSE'],
  insuranceStatus: InsuranceStatus['APPROVED'],
  unapprovalReason: 'Wooden',
  availableBalance: 7294,
  updatedAt: dayjs('2022-05-24T17:36'),
  approvedAt: dayjs('2022-05-24T11:45'),
  insuranceId: 'District',
  createdAt: dayjs('2022-05-25T04:40'),
};

export const sampleWithNewData: NewInsuranceRegistration = {
  dateOfBirth: dayjs('2022-05-25'),
  insuranceRelation: InsuranceRelation['SPOUSE'],
  insuranceStatus: InsuranceStatus['SEPARATED'],
  availableBalance: 60237,
  createdAt: dayjs('2022-05-24T13:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
