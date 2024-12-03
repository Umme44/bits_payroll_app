import dayjs from 'dayjs/esm';
import { AcknowledgementStatus } from 'app/entities/enumerations/acknowledgement-status.model';

export interface ITaxAcknowledgementReceipt {
  id: number;
  pin?: string | null;
  name?: string | null;
  designation?: string | null;
  tinNumber?: string | null;
  receiptNumber?: string | null;
  taxesCircle?: string | null;
  taxesZone?: string | null;
  dateOfSubmission?: dayjs.Dayjs | null;
  filePath?: string | null;
  acknowledgementStatus?: AcknowledgementStatus | null;
  receivedAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  // fiscalYear?: Pick<IAitConfig, 'id'> | null;
  // employee?: Pick<IEmployee, 'id'> | null;
  // receivedBy?: Pick<IUser, 'id' | 'login'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  assessmentYear?: string;
  fiscalYearId?: number;
  employeeId?: number;
  receivedByLogin?: string;
  receivedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  startYear?: number;
  endYear?: number;
  isChecked?: boolean;
}

export type NewTaxAcknowledgementReceipt = Omit<ITaxAcknowledgementReceipt, 'id'> & { id: null };
