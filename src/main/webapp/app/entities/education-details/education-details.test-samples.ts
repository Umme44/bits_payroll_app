import { IEducationDetails, NewEducationDetails } from './education-details.model';

export const sampleWithRequiredData: IEducationDetails = {
  id: 91680,
};

export const sampleWithPartialData: IEducationDetails = {
  id: 81756,
  subject: 'vertical microchip Pines',
  yearOfDegreeCompletion: 'logistical',
};

export const sampleWithFullData: IEducationDetails = {
  id: 83979,
  nameOfDegree: 'Missouri',
  subject: 'Fall',
  institute: 'Security system navigating',
  yearOfDegreeCompletion: 'SDR',
};

export const sampleWithNewData: NewEducationDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
