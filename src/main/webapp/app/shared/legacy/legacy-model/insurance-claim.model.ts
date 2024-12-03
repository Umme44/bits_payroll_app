import dayjs from 'dayjs/esm';
import { ClaimStatus } from 'app/shared/model/enumerations/claim-status.model';
import { InsuranceRelation } from '../../model/enumerations/insurance-relation.model';

export interface IInsuranceClaim {
  id?: number;
  insuranceCardId?: string;
  policyHolderPin?: string;
  policyHolderName?: string;
  registrationName?: string;
  relation?: InsuranceRelation;
  settlementDate?: dayjs.Dayjs;
  paymentDate?: dayjs.Dayjs;
  regretDate?: dayjs.Dayjs;
  regretReason?: string;
  claimedAmount?: number;
  settledAmount?: number;
  claimStatus?: ClaimStatus;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  insuranceRegistrationId?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
}

export class InsuranceClaim implements IInsuranceClaim {
  constructor(
    public id?: number,
    public insuranceCardId?: string,
    public policyHolderPin?: string,
    public policyHolderName?: string,
    public registrationName?: string,
    public relation?: InsuranceRelation,
    public settlementDate?: dayjs.Dayjs,
    public paymentDate?: dayjs.Dayjs,
    public regretDate?: dayjs.Dayjs,
    public regretReason?: string,
    public claimedAmount?: number,
    public settledAmount?: number,
    public claimStatus?: ClaimStatus,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public insuranceRegistrationId?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number
  ) {}
}
