import dayjs from 'dayjs/esm';

import { PfLoanStatus } from 'app/entities/enumerations/pf-loan-status.model';

import { IPfLoan, NewPfLoan } from './pf-loan.model';

export const sampleWithRequiredData: IPfLoan = {
  id: 89466,
};

export const sampleWithPartialData: IPfLoan = {
  id: 1665,
  disbursementDate: dayjs('2021-02-16'),
  bankAccountNumber: 'context-sensitive',
  chequeNumber: 'Rwanda Ruble',
  instalmentStartFrom: dayjs('2021-02-17'),
};

export const sampleWithFullData: IPfLoan = {
  id: 83351,
  disbursementAmount: 82799,
  disbursementDate: dayjs('2021-02-17'),
  bankName: 'parsing Planner',
  bankBranch: 'Shoes model grid-enabled',
  bankAccountNumber: 'Customer Producer',
  chequeNumber: 'open-source blue',
  instalmentNumber: 'XML lime',
  installmentAmount: 14157,
  instalmentStartFrom: dayjs('2021-02-16'),
  status: PfLoanStatus['PAID_OFF'],
};

export const sampleWithNewData: NewPfLoan = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
