export interface IYearlyTotalContribution {
  totalEmployeeContributionInYear?: number;
  totalEmployerContributionInYear?: number;
}

export class YearlyTotalContribution implements IYearlyTotalContribution {
  constructor(public totalEmployeeContributionInYear?: number, public totalEmployerContributionInYear?: number) {}
}
