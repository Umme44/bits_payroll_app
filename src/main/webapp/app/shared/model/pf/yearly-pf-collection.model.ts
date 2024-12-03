import { MonthlyContribution } from './monthly-contribution.model';
import { YearlyTotalContribution } from './yearly-total-contribution.model';

export interface IYearlyPfCollection {
  year?: number;
  monthlyContributionList?: MonthlyContribution[];
  yearlyTotalContribution?: YearlyTotalContribution;
}

export class YearlyPfCollection implements IYearlyPfCollection {
  constructor(
    public year?: number,
    public monthlyContributionList?: MonthlyContribution[],
    public yearlyTotalContribution?: YearlyTotalContribution
  ) {}
}
