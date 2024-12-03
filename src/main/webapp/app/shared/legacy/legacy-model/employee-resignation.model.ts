import { Status } from 'app/shared/model/enumerations/status.model';
import dayjs from 'dayjs/esm';

export interface IEmployeeResignation {
  id?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  resignationDate?: dayjs.Dayjs | null;
  approvalStatus?: Status;
  approvalComment?: string;
  isSalaryHold?: boolean;
  isFestivalBonusHold?: boolean;
  resignationReason?: string;
  lastWorkingDay?: dayjs.Dayjs | null;

  createdById?: number;
  uodatedById?: number;
  employeeId?: number;
  pin?: string;
  name?: string;
  designation?: string;
  unit?: string;
  department?: string;
}

export class EmployeeResignation implements IEmployeeResignation {
  constructor(
    public id?: number,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public resignationDate?: dayjs.Dayjs | null,
    public approvalStatus?: Status,
    public approvalComment?: string,
    public isSalaryHold?: boolean,
    public isFestivalBonusHold?: boolean,
    public resignationReason?: string,
    public lastWorkingDay?: dayjs.Dayjs | null,
    public createdById?: number,
    public uodatedById?: number,
    public employeeId?: number,
    public pin?: string,
    public name?: string,
    public designation?: string,
    public unit?: string,
    public department?: string
  ) {
    this.isSalaryHold = this.isSalaryHold || false;
    this.isFestivalBonusHold = this.isFestivalBonusHold || false;
  }
}
