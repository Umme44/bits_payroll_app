export interface IInsuranceConfiguration {
  id?: number;
  maxTotalClaimLimitPerYear?: number;
  maxAllowedChildAge?: number;
  insuranceClaimLink?: string;
}

export class InsuranceConfiguration implements IInsuranceConfiguration {
  constructor(
    public id?: number,
    public maxTotalClaimLimitPerYear?: number,
    public maxAllowedChildAge?: number,
    public insuranceClaimLink?: string
  ) {}
}
