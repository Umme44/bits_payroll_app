import dayjs from 'dayjs/esm';

import { PurposeOfNOC } from 'app/entities/enumerations/purpose-of-noc.model';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

import { IEmployeeNOC, NewEmployeeNOC } from './employee-noc.model';

export const sampleWithRequiredData: IEmployeeNOC = {
  id: 77585,
  passportNumber: 'Berkshire ADP Awesome',
  leaveStartDate: dayjs('2022-10-23'),
  leaveEndDate: dayjs('2022-10-22'),
  countryToVisit: 'teal Marketing e-enable',
  createdAt: dayjs('2022-10-23T01:30'),
  purposeOfNOC: PurposeOfNOC['OFFICIAL'],
  certificateStatus: CertificateStatus['DOCUMENT_PROVIDED'],
};

export const sampleWithPartialData: IEmployeeNOC = {
  id: 10590,
  passportNumber: 'deposit Minnesota transmitting',
  leaveStartDate: dayjs('2022-10-22'),
  leaveEndDate: dayjs('2022-10-22'),
  countryToVisit: 'firewall parse Kyat',
  issueDate: dayjs('2022-10-22'),
  createdAt: dayjs('2022-10-22T22:39'),
  updatedAt: dayjs('2022-10-22T12:01'),
  reason: 'Cambridgeshire withdrawal HTTP',
  purposeOfNOC: PurposeOfNOC['MEDICAL'],
  certificateStatus: CertificateStatus['SENT_FOR_GENERATION'],
};

export const sampleWithFullData: IEmployeeNOC = {
  id: 57843,
  passportNumber: 'firewall systems',
  leaveStartDate: dayjs('2022-10-22'),
  leaveEndDate: dayjs('2022-10-23'),
  countryToVisit: 'Directives Awesome (EURCO)',
  referenceNumber: 'synthesize',
  issueDate: dayjs('2022-10-23'),
  createdAt: dayjs('2022-10-22T10:59'),
  updatedAt: dayjs('2022-10-23T09:04'),
  generatedAt: dayjs('2022-10-22T10:28'),
  reason: 'Awesome Summit',
  purposeOfNOC: PurposeOfNOC['ACADEMIC'],
  certificateStatus: CertificateStatus['GENERATED'],
  isRequiredForVisa: true,
};

export const sampleWithNewData: NewEmployeeNOC = {
  passportNumber: 'Way',
  leaveStartDate: dayjs('2022-10-22'),
  leaveEndDate: dayjs('2022-10-22'),
  countryToVisit: 'silver',
  createdAt: dayjs('2022-10-23T02:29'),
  purposeOfNOC: PurposeOfNOC['OFFICIAL'],
  certificateStatus: CertificateStatus['GENERATED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
