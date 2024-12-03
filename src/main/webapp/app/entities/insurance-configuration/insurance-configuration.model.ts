export interface IInsuranceConfiguration {
  id?: number;
  maxTotalClaimLimitPerYear?: number | null;
  maxAllowedChildAge?: number | null;
  insuranceClaimLink?: string | null;
}

export type NewInsuranceConfiguration = Omit<IInsuranceConfiguration, 'id'> & { id: null };
