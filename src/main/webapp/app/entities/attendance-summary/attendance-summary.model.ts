import dayjs from 'dayjs/esm';

export interface IAttendanceSummary {
  id: number;
  month?: number | null;
  year?: number | null;
  totalWorkingDays?: number | null;
  totalLeaveDays?: number | null;
  totalAbsentDays?: number | null;
  totalFractionDays?: number | null;
  attendanceRegularisationStartDate?: dayjs.Dayjs | null;
  attendanceRegularisationEndDate?: dayjs.Dayjs | null;

  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;

  // employee?: Pick<IEmployee, 'id' | 'pin'> | null;
}

export type NewAttendanceSummary = Omit<IAttendanceSummary, 'id'> & { id: null };

export class AttendanceSummary implements IAttendanceSummary {
  constructor(
    public id: number,
    public month?: number | null,
    public year?: number | null,
    public totalWorkingDays?: number | null,
    public totalLeaveDays?: number | null,
    public totalAbsentDays?: number | null,
    public totalFractionDays?: number | null,
    public attendanceRegularisationStartDate?: dayjs.Dayjs | null,
    public attendanceRegularisationEndDate?: dayjs.Dayjs | null,

    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {}
}
