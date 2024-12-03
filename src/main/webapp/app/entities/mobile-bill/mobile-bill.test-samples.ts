import { IMobileBill, NewMobileBill } from './mobile-bill.model';

export const sampleWithRequiredData: IMobileBill = {
  id: 89805,
};

export const sampleWithPartialData: IMobileBill = {
  id: 90948,
  month: 1538,
  amount: 36553,
  year: 56749,
};

export const sampleWithFullData: IMobileBill = {
  id: 29526,
  month: 20670,
  amount: 60510,
  year: 43127,
};

export const sampleWithNewData: NewMobileBill = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
