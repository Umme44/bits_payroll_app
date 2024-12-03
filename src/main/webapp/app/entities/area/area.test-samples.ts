import { IArea, NewArea } from './area.model';

export const sampleWithRequiredData: IArea = {
  id: 11565,
  name: 'Steel African',
};

export const sampleWithPartialData: IArea = {
  id: 34278,
  name: 'methodologies Chips Ball',
};

export const sampleWithFullData: IArea = {
  id: 32492,
  name: 'Sleek',
};

export const sampleWithNewData: NewArea = {
  name: 'Liberia Operative Unbranded',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
