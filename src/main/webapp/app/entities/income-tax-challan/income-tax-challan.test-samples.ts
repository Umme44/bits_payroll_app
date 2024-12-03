import dayjs from 'dayjs/esm';

import { Month } from 'app/entities/enumerations/month.model';

import { IIncomeTaxChallan, NewIncomeTaxChallan } from './income-tax-challan.model';

export const sampleWithRequiredData: IIncomeTaxChallan = {
  id: 20336,
  challanNo: 'Reverse-engineered payment',
  challanDate: dayjs('2022-05-19'),
  amount: 97641,
  month: Month['MAY'],
  year: 2088,
};

export const sampleWithPartialData: IIncomeTaxChallan = {
  id: 18226,
  challanNo: 'firewall TCP Checking',
  challanDate: dayjs('2022-05-19'),
  amount: 84152,
  month: Month['JANUARY'],
  year: 2132,
};

export const sampleWithFullData: IIncomeTaxChallan = {
  id: 45319,
  challanNo: 'Optimization Administrator Infrastructure',
  challanDate: dayjs('2022-05-19'),
  amount: 62046,
  month: Month['JUNE'],
  year: 2077,
  remarks: 'Missouri Lead plug-and-play',
};

export const sampleWithNewData: NewIncomeTaxChallan = {
  challanNo: 'Wooden',
  challanDate: dayjs('2022-05-19'),
  amount: 62442,
  month: Month['MAY'],
  year: 2178,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
