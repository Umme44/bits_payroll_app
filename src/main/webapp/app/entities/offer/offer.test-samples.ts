import dayjs from 'dayjs/esm';

import { IOffer, NewOffer } from './offer.model';

export const sampleWithRequiredData: IOffer = {
  id: 85569,
  description: 'Gorgeous Analyst Massachusetts',
};

export const sampleWithPartialData: IOffer = {
  id: 35087,
  description: 'parsing',
  createdAt: dayjs('2022-04-04'),
};

export const sampleWithFullData: IOffer = {
  id: 91623,
  title: 'quantify interface',
  description: 'Games stable Somoni',
  imagePath: 'platforms Rupee feed',
  createdAt: dayjs('2022-04-04'),
};

export const sampleWithNewData: NewOffer = {
  description: 'Organic model',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
