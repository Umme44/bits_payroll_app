import { IFestivalBonusDetails, NewFestivalBonusDetails } from './festival-bonus-details.model';

export const sampleWithRequiredData: IFestivalBonusDetails = {
  id: 31783,
  bonusAmount: 27896,
  isHold: false,
};

export const sampleWithPartialData: IFestivalBonusDetails = {
  id: 89922,
  bonusAmount: 35362,
  isHold: true,
  gross: 50581,
};

export const sampleWithFullData: IFestivalBonusDetails = {
  id: 98995,
  bonusAmount: 5710,
  remarks: 'Functionality bus',
  isHold: true,
  basic: 62434,
  gross: 20274,
};

export const sampleWithNewData: NewFestivalBonusDetails = {
  bonusAmount: 26204,
  isHold: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
