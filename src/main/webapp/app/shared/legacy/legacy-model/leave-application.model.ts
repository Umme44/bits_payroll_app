import dayjs from 'dayjs/esm';
import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';

export interface ILeaveApplication {
  id?: number;
  applicationDate?: dayjs.Dayjs;
  leaveType?: LeaveType;
  description?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  approveDate?: dayjs.Dayjs;
  isLineManagerApproved?: boolean;
  isHRApproved?: boolean;
  isRejected?: boolean;
  rejectionComment?: string;
  isHalfDay?: boolean;
  durationInDay?: number;
  phoneNumberOnLeave?: string;
  addressOnLeave?: any;
  reason?: any;
  sanctionedAt?: dayjs.Dayjs;
  employeeId?: number;
  sanctionedByLogin?: string;
  sanctionedById?: number;
  isChecked?: Boolean;
  sanctionedBy?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export class LeaveApplication implements ILeaveApplication {
  constructor(
    public id?: number,
    public applicationDate?: dayjs.Dayjs,
    public leaveType?: LeaveType,
    public description?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public isLineManagerApproved?: boolean,
    public isHRApproved?: boolean,
    public isRejected?: boolean,
    public rejectionComment?: string,
    public isHalfDay?: boolean,
    public durationInDay?: number,
    public phoneNumberOnLeave?: string,
    public addressOnLeave?: any,
    public reason?: any,
    public sanctionedAt?: dayjs.Dayjs,
    public employeeId?: number,
    public sanctionedByLogin?: string,
    public sanctionedById?: number,
    public isChecked?: Boolean,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {
    this.isLineManagerApproved = this.isLineManagerApproved || false;
    this.isHRApproved = this.isHRApproved || false;
    this.isRejected = this.isRejected || false;
    this.isHalfDay = this.isHalfDay || false;
    this.isChecked = this.isChecked || false;
  }
}
