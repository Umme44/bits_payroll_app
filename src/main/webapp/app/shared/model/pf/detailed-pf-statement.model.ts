import dayjs from 'dayjs/esm';
import { TotalPf } from './total-pf.model';
import { YearlyPfCollectionForDetailedPfStatement } from './yearly-pf-collection-for-detailed-pf-statement.model';

export interface IDetailedPfStatement {
  name?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  openingBalance?: number;
  yearlyPfCollection?: YearlyPfCollectionForDetailedPfStatement[];
  totalContribution?: TotalPf;
  totalPfInterest?: TotalPf;
  totalContributionWithInterests?: TotalPf;

  adjustmentForArrearsPf?: TotalPf;
  netPfPayable?: number;
}

export class DetailedPfStatement implements IDetailedPfStatement {
  constructor(
    public name?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public openingBalance?: number,
    public yearlyPfCollection?: YearlyPfCollectionForDetailedPfStatement[],
    public totalContribution?: TotalPf,
    public totalPfInterest?: TotalPf,
    public totalContributionWithInterests?: TotalPf,

    public adjustmentForArrearsPf?: TotalPf,
    public netPfPayable?: number
  ) {}
}
