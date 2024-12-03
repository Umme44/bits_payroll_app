import { InsuranceStatus } from '../enumerations/insurance-status.model';

export interface IInsuranceApprovalDto {
  insuranceCardId?: string;
  registrationId?: number;
  claimId?: number;
  status?: InsuranceStatus;
  reason?: string;
  acceptedAmount?: number;
}

export class InsuranceApprovalDto implements IInsuranceApprovalDto {
  constructor(
    public insuranceCardId?: string,
    public registrationId?: number,
    public claimId?: number,
    public status?: InsuranceStatus,
    public reason?: string,
    public acceptedAmount?: number
  ) {}
}
