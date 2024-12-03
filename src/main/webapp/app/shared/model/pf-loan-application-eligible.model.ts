import { EmployeeCategory } from '../../entities/enumerations/employee-category.model';


export interface IPfLoanApplicationEligible {
  employeeCategory?: EmployeeCategory;
  regularConfirmedEmployee?: boolean;
  eligibleBand?: boolean;
  bandName?: number;
  pfAccountMatured?: boolean;
  serviceTenure?: string;
  anyOpenRepayingPfLoan?: boolean;
  pfLoanEligibleAmount?: number;
}

export class PfLoanApplicationEligible {
  constructor(
    public employeeCategory?: EmployeeCategory,
    public regularConfirmedEmployee?: boolean,
    public eligibleBand?: boolean,
    public bandName?: number,
    public pfAccountMatured?: boolean,
    public serviceTenure?: string,
    public anyOpenRepayingPfLoan?: boolean,
    public pfLoanEligibleAmount?: number
  ) {}
}
