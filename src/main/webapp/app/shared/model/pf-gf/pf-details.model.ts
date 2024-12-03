import { PfContribution } from './pf-contribution.model';
import { PfInterest } from './pf-internest.model';

export interface IPfDetails {
  openingBalance?: number;
  pfContributionList?: PfContribution[];
  pfArrear?: number;
  pfInterest?: PfInterest;
  totalPfPayable?: number;
  remarks?: String;
}

export class PfDetails implements IPfDetails {
  constructor(
    public openingBalance?: number,
    public pfContributionList?: PfContribution[],
    public pfArrear?: number,
    public pfInterest?: PfInterest,
    public totalPfPayable?: number,
    public remarks?: String
  ) {}
}
