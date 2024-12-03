import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface ISalaryCertificate {
  id: number;
  purpose?: string | null;
  remarks?: string | null;
  status?: Status | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  sanctionAt?: dayjs.Dayjs | null;
  month?: Month | null;
  year?: number | null;

  referenceNumber?: string;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  sanctionByLogin?: string;
  sanctionById?: number;

  employeeName?: string;
  pin?: string;
  designationName?: string;
  employeeId?: number;

  salaryId?: number;
  isChecked?: boolean;

  basic?: number;
  houseRent?: number;
  medicalAllowance?: number;
  conveyanceAllowance?: number;
  entertainment?: number;
  utility?: number;
  otherAddition?: number;
  grossPay?: number;

  incomeTax?: number;
  pfDeduction?: number;
  mobileBill?: number;
  welfareFund?: number;
  otherDeduction?: number;
  totalDeduction?: number;
  netPayable?: number;

  dateOfJoining?: dayjs.Dayjs | null;

  signatoryPersonId?: number;
  signatoryPersonName?: string;
  signatoryPersonDesignation?: string;

  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // sanctionBy?: Pick<IUser, 'id' | 'login'> | null;
  // employee?: Pick<IEmployee, 'id'> | null;
  // signatoryPerson?: Pick<IEmployee, 'id'> | null;
}

export type NewSalaryCertificate = Omit<ISalaryCertificate, 'id'> & { id: null };
