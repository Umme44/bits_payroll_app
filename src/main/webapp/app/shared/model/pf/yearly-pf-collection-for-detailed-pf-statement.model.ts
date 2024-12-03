import { MonthlyContribution } from './monthly-contribution.model';
import { YearlyTotalContribution } from './yearly-total-contribution.model';
import { YearlyTotalInterest } from './yearly-total-interest.model';
import { YearlyTotalArrearPfDeduction } from './yearly-total-arrear-pf-deduction.model';

export interface IYearlyPfCollectionForDetailedPfStatement {
  year?: number;
  monthlyContributionList?: MonthlyContribution[];
  yearlyTotalContribution?: YearlyTotalContribution;
  yearlyTotalArrearPfDeduction?: YearlyTotalArrearPfDeduction;
  yearlyTotalInterest?: YearlyTotalInterest;
}

export class YearlyPfCollectionForDetailedPfStatement implements IYearlyPfCollectionForDetailedPfStatement {
  constructor(
    public year?: number,
    public monthlyContributionList?: MonthlyContribution[],
    public yearlyTotalContribution?: YearlyTotalContribution,
    public yearlyTotalArrearPfDeduction?: YearlyTotalArrearPfDeduction,
    public yearlyTotalInterest?: YearlyTotalInterest
  ) {}
}
