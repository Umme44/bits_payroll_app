import dayjs from 'dayjs/esm';

export interface IAttendanceSummary {
  id?: number;
  month?: number;
  year?: number;
  totalWorkingDays?: number;
  totalLeaveDays?: number;
  totalAbsentDays?: number;
  totalFractionDays?: number;
  attendanceRegularisationStartDate?: dayjs.Dayjs;
  attendanceRegularisationEndDate?: dayjs.Dayjs;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export class AttendanceSummary implements IAttendanceSummary {
  constructor(
    public id?: number,
    public month?: number,
    public year?: number,
    public totalWorkingDays?: number,
    public totalLeaveDays?: number,
    public totalAbsentDays?: number,
    public totalFractionDays?: number,
    public attendanceRegularisationStartDate?: dayjs.Dayjs,
    public attendanceRegularisationEndDate?: dayjs.Dayjs,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {}
}
