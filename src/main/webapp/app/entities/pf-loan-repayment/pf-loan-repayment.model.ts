import dayjs from 'dayjs/esm';
import { IPfLoan } from 'app/entities/pf-loan/pf-loan.model';
import { PfRepaymentStatus } from 'app/entities/enumerations/pf-repayment-status.model';
import { PfAccountStatus } from '../enumerations/pf-account-status.model';
import { PfLoanStatus } from '../enumerations/pf-loan-status.model';

export interface IPfLoanRepayment {
  id: number;
  amount?: number | null;
  status?: PfRepaymentStatus | null;
  deductionMonth?: number | null;
  deductionYear?: number | null;
  deductionDate?: dayjs.Dayjs | null;
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

export type NewPfLoanRepayment = Omit<IPfLoanRepayment, 'id'> & { id: null };
