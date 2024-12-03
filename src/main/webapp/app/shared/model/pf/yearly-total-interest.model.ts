export interface IYearlyTotalInterest {
  totalEmployeeInterestInYear?: number;
  totalEmployerInterestInYear?: number;
}

export class YearlyTotalInterest implements IYearlyTotalInterest {
  constructor(public totalEmployeeInterestInYear?: number, public totalEmployerInterestInYear?: number) {}
}
