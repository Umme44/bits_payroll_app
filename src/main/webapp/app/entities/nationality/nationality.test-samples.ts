import { INationality, NewNationality } from './nationality.model';

export const sampleWithRequiredData: INationality = {
  id: 30424,
  nationalityName: 'Louisiana National',
};

export const sampleWithPartialData: INationality = {
  id: 99167,
  nationalityName: 'Escudo Automotive partner',
};

export const sampleWithFullData: INationality = {
  id: 10373,
  nationalityName: 'Designer',
};

export const sampleWithNewData: NewNationality = {
  nationalityName: 'Investor azure schemas',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
