import dayjs from 'dayjs/esm';
import { IInsuranceRegistration } from 'app/entities/insurance-registration/insurance-registration.model';
import { IUser } from 'app/entities/user/user.model';
import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';
import {InsuranceRelation} from "../../shared/model/enumerations/insurance-relation.model";

export interface IInsuranceClaim {
  id: number;
  settlementDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  regretDate?: dayjs.Dayjs | null;
  regretReason?: string | null;
  claimedAmount?: number | null;
  settledAmount?: number | null;
  claimStatus?: ClaimStatus | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;

  // insuranceRegistration?: Pick<IInsuranceRegistration, 'id'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;

  insuranceCardId?: string;
  policyHolderPin?: string;
  policyHolderName?: string;
  registrationName?: string;
  relation?: InsuranceRelation;
  insuranceRegistrationId?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
}

export type NewInsuranceClaim = Omit<IInsuranceClaim, 'id'> & { id: null };
