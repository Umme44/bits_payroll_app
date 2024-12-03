import dayjs from 'dayjs/esm';

import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';

import { IInsuranceClaim, NewInsuranceClaim } from './insurance-claim.model';

export const sampleWithRequiredData: IInsuranceClaim = {
  id: 51310,
};

export const sampleWithPartialData: IInsuranceClaim = {
  id: 1490,
  regretDate: dayjs('2022-05-24'),
  regretReason: 'Rubber',
  settledAmount: 36369,
  claimStatus: ClaimStatus['SETTLED'],
};

export const sampleWithFullData: IInsuranceClaim = {
  id: 23251,
  settlementDate: dayjs('2022-05-24'),
  paymentDate: dayjs('2022-05-25'),
  regretDate: dayjs('2022-05-25'),
  regretReason: 'Dollar',
  claimedAmount: 71659,
  settledAmount: 52013,
  claimStatus: ClaimStatus['SETTLED'],
  createdAt: dayjs('2022-05-24T22:43'),
  updatedAt: dayjs('2022-05-24T20:00'),
};

export const sampleWithNewData: NewInsuranceClaim = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
