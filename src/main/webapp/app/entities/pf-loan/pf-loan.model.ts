import dayjs from 'dayjs/esm';
import { IPfLoanApplication } from 'app/entities/pf-loan-application/pf-loan-application.model';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfLoanStatus } from 'app/entities/enumerations/pf-loan-status.model';

export interface IPfLoan {
  id: number;
  disbursementAmount?: number | null;
  disbursementDate?: dayjs.Dayjs | null;
  bankName?: string | null;
  bankBranch?: string | null;
  bankAccountNumber?: string | null;
  chequeNumber?: string | null;
  instalmentNumber?: string | null;
  installmentAmount?: number | null;
  instalmentStartFrom?: dayjs.Dayjs | null;
  status?: PfLoanStatus | null;
  pfLoanApplicationId?: number;
  pfAccountId?: number;

  pfCode?: string;
  accountStatus?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
}

export type NewPfLoan = Omit<IPfLoan, 'id'> & { id: null };
