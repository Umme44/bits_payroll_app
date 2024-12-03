import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IManualAttendanceEntry {
  id: number;
  date?: dayjs.Dayjs | null;
  inTime?: dayjs.Dayjs | null;
  inNote?: string | null;
  outTime?: dayjs.Dayjs | null;
  outNote?: string | null;
  isLineManagerApproved?: boolean | null;
  isHRApproved?: boolean | null;
  isRejected?: boolean | null;
  rejectionComment?: string | null;
  note?: string | null;
  employee?: Pick<IEmployee, 'id' | 'fullName'> | null;
}

export type NewManualAttendanceEntry = Omit<IManualAttendanceEntry, 'id'> & { id: null };
