import dayjs from 'dayjs/esm';
import { PfRepaymentStatus } from 'app/shared/model/enumerations/pf-repayment-status.model';
import { PfAccountStatus } from 'app/shared/model/enumerations/pf-account-status.model';
import { PfLoanStatus } from 'app/shared/model/enumerations/pf-loan-status.model';

export interface IPfLoanRepayment {
  id?: number;
  amount?: number;
  status?: PfRepaymentStatus;
  deductionMonth?: number;
  deductionYear?: number;
  deductionDate?: dayjs.Dayjs;
  pfLoanId?: number;
  pfAccountId?: number;
  pfCode?: string;
  accountStatus?: PfAccountStatus;
  pfLoanStatus?: PfLoanStatus;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
}

export class PfLoanRepayment implements IPfLoanRepayment {
  constructor(
    public id?: number,
    public amount?: number,
    public status?: PfRepaymentStatus,
    public deductionMonth?: number,
    public deductionYear?: number,
    public deductionDate?: dayjs.Dayjs,
    public pfLoanId?: number,
    public pfAccountId?: number,
    public pfCode?: string,
    public accountStatus?: PfAccountStatus,
    public pfLoanStatus?: PfLoanStatus,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public accHolderName?: string,
    public pin?: string
  ) {}
}
