import dayjs from 'dayjs/esm';

import { ArrearPaymentType } from 'app/entities/enumerations/arrear-payment-type.model';
import { Month } from 'app/entities/enumerations/month.model';
import { Status } from 'app/entities/enumerations/status.model';

import { IArrearPayment, NewArrearPayment } from './arrear-payment.model';

export const sampleWithRequiredData: IArrearPayment = {
  id: 47929,
  paymentType: ArrearPaymentType['INDIVIDUAL'],
  disbursementAmount: 884747,
  isDeleted: false,
  arrearPF: 40668479,
  taxDeduction: 51194677,
  deductTaxUponPayment: false,
};

export const sampleWithPartialData: IArrearPayment = {
  id: 70736,
  paymentType: ArrearPaymentType['INDIVIDUAL'],
  salaryYear: 1900,
  approvalStatus: Status['NOT_APPROVED'],
  disbursementAmount: 28545434,
  isDeleted: true,
  arrearPF: 31052096,
  taxDeduction: 26120118,
  deductTaxUponPayment: false,
};

export const sampleWithFullData: IArrearPayment = {
  id: 78248,
  paymentType: ArrearPaymentType['ALONGSIDE_MONTHLY_SALARY'],
  disbursementDate: dayjs('2022-03-07'),
  salaryMonth: Month['AUGUST'],
  salaryYear: 2183,
  approvalStatus: Status['APPROVED'],
  disbursementAmount: 51502306,
  isDeleted: true,
  arrearPF: 54433766,
  taxDeduction: 82206335,
  deductTaxUponPayment: true,
};

export const sampleWithNewData: NewArrearPayment = {
  paymentType: ArrearPaymentType['ALONGSIDE_MONTHLY_SALARY'],
  disbursementAmount: 65519879,
  isDeleted: false,
  arrearPF: 40790434,
  taxDeduction: 96573980,
  deductTaxUponPayment: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
