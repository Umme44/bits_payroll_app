import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';
import { AttendanceDeviceOrigin } from 'app/shared/model/enumerations/attendance-device-origin.model';

export interface IAttendanceEntry {
  id: number;
  date?: dayjs.Dayjs | null;
  inTime?: dayjs.Dayjs | null;
  inNote?: string | null;
  outTime?: dayjs.Dayjs | null;
  outNote?: string | null;
  status?: Status | null;
  note?: string | null;
  punchInDeviceOrigin?: AttendanceDeviceOrigin;
  punchOutDeviceOrigin?: AttendanceDeviceOrigin;
  isAutoPunchOut?: boolean;
  employeeId?: number | null;
  pin?: string | null;
  fullName?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
  bandName?: string | null;
}

export type NewAttendanceEntry = Omit<IAttendanceEntry, 'id'> & { id: null };
