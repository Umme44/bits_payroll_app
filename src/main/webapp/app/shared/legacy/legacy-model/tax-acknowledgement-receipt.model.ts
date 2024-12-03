import dayjs from 'dayjs/esm';
import { AcknowledgementStatus } from 'app/shared/model/enumerations/acknowledgement-status.model';

export interface ITaxAcknowledgementReceipt {
  id?: number;
  pin?: string;
  name?: string;
  designation?: string;
  tinNumber?: string;
  receiptNumber?: string;
  taxesCircle?: string;
  taxesZone?: string;
  dateOfSubmission?: dayjs.Dayjs;
  filePath?: any;
  acknowledgementStatus?: AcknowledgementStatus;
  receivedAt?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
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

export class TaxAcknowledgementReceipt implements ITaxAcknowledgementReceipt {
  constructor(
    public id?: number,
    public pin?: string,
    public name?: string,
    public designation?: string,
    public tinNumber?: string,
    public receiptNumber?: string,
    public taxesCircle?: string,
    public taxesZone?: string,
    public dateOfSubmission?: dayjs.Dayjs,
    public filePath?: any,
    public acknowledgementStatus?: AcknowledgementStatus,
    public receivedAt?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public fiscalYearId?: number,
    public employeeId?: number,
    public receivedByLogin?: string,
    public receivedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public startYear?: number,
    public endYear?: number,
    public isChecked?: boolean
  ) {}
}
