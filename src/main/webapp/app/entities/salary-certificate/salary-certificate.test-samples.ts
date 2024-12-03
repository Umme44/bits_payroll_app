import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';
import { Month } from 'app/entities/enumerations/month.model';

import { ISalaryCertificate, NewSalaryCertificate } from './salary-certificate.model';

export const sampleWithRequiredData: ISalaryCertificate = {
  id: 28312,
  purpose: 'Avon Multi-tiered mint',
  status: Status['PENDING'],
  month: Month['DECEMBER'],
  year: 86963,
};

export const sampleWithPartialData: ISalaryCertificate = {
  id: 28102,
  purpose: 'Awesome Loan transmitter',
  remarks: 'applications',
  status: Status['PENDING'],
  updatedAt: dayjs('2022-01-23'),
  sanctionAt: dayjs('2022-01-23'),
  month: Month['JULY'],
  year: 93987,
  referenceNumber: 'Intelligent Industrial Dakota',
};

export const sampleWithFullData: ISalaryCertificate = {
  id: 38671,
  purpose: 'XSS technologies lime',
  remarks: 'program',
  status: Status['APPROVED'],
  createdAt: dayjs('2022-01-24'),
  updatedAt: dayjs('2022-01-24'),
  sanctionAt: dayjs('2022-01-23'),
  month: Month['MAY'],
  year: 72903,
  referenceNumber: 'web-enabled',
};

export const sampleWithNewData: NewSalaryCertificate = {
  purpose: 'GB lime exploit',
  status: Status['NOT_APPROVED'],
  month: Month['JULY'],
  year: 31499,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
