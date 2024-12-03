export interface IYearlyTotalArrearPfDeduction {
  totalEmployeeArrearPfDeduction?: number;
  totalEmployerArrearPfDeduction?: number;
}

export class YearlyTotalArrearPfDeduction implements IYearlyTotalArrearPfDeduction {
  constructor(public totalEmployeeArrearPfDeduction?: number, public totalEmployerArrearPfDeduction?: number) {}
}
