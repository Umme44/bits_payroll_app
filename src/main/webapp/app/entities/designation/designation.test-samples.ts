import { IDesignation, NewDesignation } from './designation.model';

export const sampleWithRequiredData: IDesignation = {
  id: 18278,
  designationName: 'zero HTTP Iowa',
};

export const sampleWithPartialData: IDesignation = {
  id: 38192,
  designationName: 'lavender content-based Infrastructure',
};

export const sampleWithFullData: IDesignation = {
  id: 18407,
  designationName: 'disintermediate',
};

export const sampleWithNewData: NewDesignation = {
  designationName: 'THX',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
