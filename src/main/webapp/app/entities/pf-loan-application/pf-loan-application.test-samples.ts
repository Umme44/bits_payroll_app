import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IPfLoanApplication, NewPfLoanApplication } from './pf-loan-application.model';

export const sampleWithRequiredData: IPfLoanApplication = {
  id: 75568,
};

export const sampleWithPartialData: IPfLoanApplication = {
  id: 53364,
  installmentAmount: 69112,
  noOfInstallment: 21967,
  recommendationDate: dayjs('2021-04-17'),
  approvalDate: dayjs('2021-04-17'),
  isRejected: false,
  rejectionReason: 'Bhutan',
  rejectionDate: dayjs('2021-04-17'),
  status: Status['PENDING'],
};

export const sampleWithFullData: IPfLoanApplication = {
  id: 23047,
  installmentAmount: 8492,
  noOfInstallment: 42158,
  remarks: 'bypassing Applications Optimization',
  isRecommended: false,
  recommendationDate: dayjs('2021-04-17'),
  isApproved: false,
  approvalDate: dayjs('2021-04-17'),
  isRejected: true,
  rejectionReason: 'Cambridgeshire azure',
  rejectionDate: dayjs('2021-04-17'),
  disbursementDate: dayjs('2021-04-17'),
  disbursementAmount: 54721,
  status: Status['PENDING'],
};

export const sampleWithNewData: NewPfLoanApplication = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
