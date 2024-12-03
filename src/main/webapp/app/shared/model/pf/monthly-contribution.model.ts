export interface IMonthlyContribution {
  Month?: string;
  grossSalary?: number;
  basic?: number;

  employeeInterest?: number;
  employeeContribution?: number;

  employerInterest?: number;
  employerContribution?: number;

  employeePfArrear?: number;
  employerPfArrear?: number;

  totalInterest?: number;
  totalContribution?: number;
  remarks?: string;
}

export class MonthlyContribution implements IMonthlyContribution {
  constructor(
    public month?: string,
    public grossSalary?: number,
    public basic?: number,

    public employeeInterest?: number,
    public employeeContribution?: number,

    public employerInterest?: number,
    public employerContribution?: number,

    public employeePfArrear?: number,
    public employerPfArrear?: number,

    public totalInterest?: number,
    public totalContribution?: number,
    public remarks?: string
  ) {}
}
