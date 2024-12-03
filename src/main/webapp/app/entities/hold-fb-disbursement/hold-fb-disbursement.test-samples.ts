import dayjs from 'dayjs/esm';

import { IHoldFbDisbursement, NewHoldFbDisbursement } from './hold-fb-disbursement.model';

export const sampleWithRequiredData: IHoldFbDisbursement = {
  id: 98840,
  disbursedAt: dayjs('2022-04-10'),
};

export const sampleWithPartialData: IHoldFbDisbursement = {
  id: 43548,
  disbursedAt: dayjs('2022-04-10'),
};

export const sampleWithFullData: IHoldFbDisbursement = {
  id: 89401,
  disbursedAt: dayjs('2022-04-11'),
  remarks: 'bandwidth seamless',
};

export const sampleWithNewData: NewHoldFbDisbursement = {
  disbursedAt: dayjs('2022-04-10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
