export class UserPfCollectionYearly implements IUserPfCollectionYearly {
  constructor(
    public year?: number,
    public totalEmployeeContributionInYear?: number,
    public totalEmployerContributionInYear?: number,
    public totalContributionInYear?: number,
    public incomeForTheYear?: number
  ) {}
}

export interface IUserPfCollectionYearly {
  year?: number;
  totalEmployeeContributionInYear?: number;
  totalEmployerContributionInYear?: number;
  totalContributionInYear?: number;
  incomeForTheYear?: number;
}
