import dayjs from 'dayjs/esm';
import { PfDetails } from './pf-details.model';

export interface IPfGfStatement {
  name?: string;
  pin?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  lastWorkingDay?: dayjs.Dayjs;
  pfRulesEffectiveFrom?: dayjs.Dayjs;
  gfRulesEffectiveFrom?: dayjs.Dayjs;
  pfEntitlementTenure?: string;
  pfEntitlementTenureInDays?: number;
  gfEntitlementTenure?: string;
  gfEntitlementTenureInDays?: number;
  totalPayablePfAndGf?: number;
  serviceLengthInYear?: number;
  lastBasic?: number;
  totalGfPayable?: number;
  pfDetails?: PfDetails;
}

export class PfGfStatement implements IPfGfStatement {
  constructor(
    public name?: string,
    public pin?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public lastWorkingDay?: dayjs.Dayjs,
    public pfRulesEffectiveFrom?: dayjs.Dayjs,
    public gfRulesEffectiveFrom?: dayjs.Dayjs,
    public pfEntitlementTenure?: string,
    public pfEntitlementTenureInDays?: number,
    public gfEntitlementTenure?: string,
    public gfEntitlementTenureInDays?: number,
    public totalPayablePfAndGf?: number,
    public serviceLengthInYear?: number,
    public lastBasic?: number,
    public totalGfPayable?: number,
    public pfDetails?: PfDetails
  ) {}
}
