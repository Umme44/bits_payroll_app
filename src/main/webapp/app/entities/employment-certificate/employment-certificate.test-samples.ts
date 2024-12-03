import dayjs from 'dayjs/esm';

import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

import { IEmploymentCertificate, NewEmploymentCertificate } from './employment-certificate.model';

export const sampleWithRequiredData: IEmploymentCertificate = {
  id: 24730,
  certificateStatus: CertificateStatus['GENERATED'],
  createdAt: dayjs('2022-10-24T16:59'),
};

export const sampleWithPartialData: IEmploymentCertificate = {
  id: 71680,
  certificateStatus: CertificateStatus['SENT_FOR_GENERATION'],
  referenceNumber: 'International Shoes Dollar',
  reason: 'visualize Lane',
  createdAt: dayjs('2022-10-24T15:41'),
  updatedAt: dayjs('2022-10-25T07:47'),
};

export const sampleWithFullData: IEmploymentCertificate = {
  id: 92392,
  certificateStatus: CertificateStatus['SENT_FOR_GENERATION'],
  referenceNumber: 'attitude-oriented 24 Washington',
  issueDate: dayjs('2022-10-24'),
  reason: 'Soft Electronics schemas',
  createdAt: dayjs('2022-10-24T18:31'),
  updatedAt: dayjs('2022-10-24T21:48'),
  generatedAt: dayjs('2022-10-24T14:29'),
};

export const sampleWithNewData: NewEmploymentCertificate = {
  certificateStatus: CertificateStatus['REJECTED'],
  createdAt: dayjs('2022-10-25T03:53'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
