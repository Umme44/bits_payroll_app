import dayjs from 'dayjs/esm';

import { IAitPayment, NewAitPayment } from './ait-payment.model';

export const sampleWithRequiredData: IAitPayment = {
  id: 17892,
};

export const sampleWithPartialData: IAitPayment = {
  id: 33350,
  date: dayjs('2021-08-23'),
  description: 'Baby calculate',
};

export const sampleWithFullData: IAitPayment = {
  id: 23111,
  date: dayjs('2021-08-23'),
  amount: 95303,
  description: 'radical Cambridgeshire',
};

export const sampleWithNewData: NewAitPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
