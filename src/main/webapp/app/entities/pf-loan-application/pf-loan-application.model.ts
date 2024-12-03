import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPfLoanApplication {
  id: number;
  installmentAmount?: number | null;
  noOfInstallment?: number | null;
  remarks?: string | null;
  isRecommended?: boolean | null;
  recommendationDate?: dayjs.Dayjs | null;
  isApproved?: boolean | null;
  approvalDate?: dayjs.Dayjs | null;
  isRejected?: boolean | null;
  rejectionReason?: string | null;
  rejectionDate?: dayjs.Dayjs | null;
  disbursementDate?: dayjs.Dayjs | null;
  disbursementAmount?: number | null;
  status?: Status | null;
  recommendedById?: number;
  approvedById?: number;
  approvedByFullName?: string;
  rejectedById?: number;
  rejectedByFullName?: number;
  pfAccountId?: number;

  pfCode?: string;
  accountStatus?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
}

export type NewPfLoanApplication = Omit<IPfLoanApplication, 'id'> & { id: null };
