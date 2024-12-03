import dayjs from 'dayjs/esm';

import { PfRepaymentStatus } from 'app/entities/enumerations/pf-repayment-status.model';

import { IPfLoanRepayment, NewPfLoanRepayment } from './pf-loan-repayment.model';

export const sampleWithRequiredData: IPfLoanRepayment = {
  id: 16487,
};

export const sampleWithPartialData: IPfLoanRepayment = {
  id: 81516,
  deductionDate: dayjs('2021-02-17'),
};

export const sampleWithFullData: IPfLoanRepayment = {
  id: 52825,
  amount: 28295,
  status: PfRepaymentStatus['APPROVED'],
  deductionMonth: 94887,
  deductionYear: 75894,
  deductionDate: dayjs('2021-02-16'),
};

export const sampleWithNewData: NewPfLoanRepayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
