import { IDeductionType, NewDeductionType } from './deduction-type.model';

export const sampleWithRequiredData: IDeductionType = {
  id: 21760,
  name: 'Ergonomic software',
};

export const sampleWithPartialData: IDeductionType = {
  id: 21887,
  name: 'Tunisia',
};

export const sampleWithFullData: IDeductionType = {
  id: 76045,
  name: 'back-end Central',
};

export const sampleWithNewData: NewDeductionType = {
  name: 'microchip',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
