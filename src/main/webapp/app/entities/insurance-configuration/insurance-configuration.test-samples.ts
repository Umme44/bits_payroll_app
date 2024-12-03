import { IInsuranceConfiguration, NewInsuranceConfiguration } from './insurance-configuration.model';

export const sampleWithRequiredData: IInsuranceConfiguration = {
  id: 14067,
  maxTotalClaimLimitPerYear: 20337,
};

export const sampleWithPartialData: IInsuranceConfiguration = {
  id: 30112,
  maxTotalClaimLimitPerYear: 61949,
  insuranceClaimLink: 'Garden hacking Handmade',
};

export const sampleWithFullData: IInsuranceConfiguration = {
  id: 64340,
  maxTotalClaimLimitPerYear: 76224,
  maxAllowedChildAge: 15771,
  insuranceClaimLink: 'Handcrafted',
};

export const sampleWithNewData: NewInsuranceConfiguration = {
  maxTotalClaimLimitPerYear: 39640,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
