import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface ISalaryCertificate {
  id?: number;
  purpose?: string;
  remarks?: string;
  status?: Status;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  sanctionAt?: dayjs.Dayjs;
  month?: Month;
  year?: number;
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

  dateOfJoining?: dayjs.Dayjs;

  signatoryPersonId?: number;
  signatoryPersonName?: string;
  signatoryPersonDesignation?: string;
}

export type NewSalaryCertificate = Omit<ISalaryCertificate, 'id'> & { id: null };
