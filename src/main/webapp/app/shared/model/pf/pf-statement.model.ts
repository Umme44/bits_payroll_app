import { YearlyPfCollection } from './yearly-pf-collection.model';
import { TotalPf } from './total-pf.model';
import dayjs from 'dayjs/esm';

export interface IPfStatement {
  name?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  openingBalance?: number;
  yearlyPfCollection?: YearlyPfCollection[];
  totalContribution?: TotalPf;
  totalPfInterest?: TotalPf;
  totalContributionWithInterests?: TotalPf;

  adjustmentForArrearsPf?: number;
  netPfPayable?: number;
}

export class PfStatement implements IPfStatement {
  constructor(
    public name?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public openingBalance?: number,
    public yearlyPfCollection?: YearlyPfCollection[],
    public totalContribution?: TotalPf,
    public totalPfInterest?: TotalPf,
    public totalContributionWithInterests?: TotalPf,

    public adjustmentForArrearsPf?: number,
    public netPfPayable?: number
  ) {}
}
