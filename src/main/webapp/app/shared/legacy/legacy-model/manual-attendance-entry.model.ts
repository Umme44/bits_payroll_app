import dayjs from 'dayjs/esm';

export interface IManualAttendanceEntry {
  id?: number;
  date?: dayjs.Dayjs;
  inTime?: dayjs.Dayjs;
  inNote?: string;
  outTime?: dayjs.Dayjs;
  outNote?: string;
  isLineManagerApproved?: boolean;
  isHRApproved?: boolean;
  isRejected?: boolean;
  rejectionComment?: string;
  note?: string;
  employeeId?: number;

  isChecked?: Boolean;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export class ManualAttendanceEntry implements IManualAttendanceEntry {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public inTime?: dayjs.Dayjs,
    public inNote?: string,
    public outTime?: dayjs.Dayjs,
    public outNote?: string,
    public isLineManagerApproved?: boolean,
    public isHRApproved?: boolean,
    public isRejected?: boolean,
    public rejectionComment?: string,
    public note?: string,
    public employeeId?: number
  ) {
    this.isLineManagerApproved = this.isLineManagerApproved || false;
    this.isHRApproved = this.isHRApproved || false;
    this.isRejected = this.isRejected || false;
  }
}
