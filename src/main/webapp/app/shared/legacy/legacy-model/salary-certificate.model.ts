import { Status } from 'app/shared/model/enumerations/status.model';
import { Month } from 'app/shared/model/enumerations/month.model';
import dayjs from 'dayjs/esm';

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

export class SalaryCertificate implements ISalaryCertificate {
  constructor(
    public id?: number,
    public purpose?: string,
    public remarks?: string,
    public status?: Status,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public sanctionAt?: dayjs.Dayjs,
    public month?: Month,
    public year?: number,
    public referenceNumber?: string,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public sanctionByLogin?: string,
    public sanctionById?: number,
    public isChecked?: boolean,
    public employeeName?: string,
    public pin?: string,
    public designationName?: string,
    public employeeId?: number,
    public salaryId?: number,

    public basic?: number,
    public houseRent?: number,
    public medicalAllowance?: number,
    public conveyanceAllowance?: number,
    public entertainment?: number,
    public utility?: number,
    public otherAddition?: number,
    public grossPay?: number,

    public incomeTax?: number,
    public pfDeduction?: number,
    public mobileBill?: number,
    public welfareFund?: number,
    public otherDeduction?: number,
    public totalDeduction?: number,
    public netPayable?: number,

    public dateOfJoining?: dayjs.Dayjs,

    public signatoryPersonId?: number,
    public signatoryPersonName?: string,
    public signatoryPersonDesignation?: string
  ) {}
}
