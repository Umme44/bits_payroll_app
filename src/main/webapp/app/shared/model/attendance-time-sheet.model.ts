import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';
import { AttendanceStatus } from './enumerations/attendance-status.model';
import { AttendanceDeviceOrigin } from './enumerations/attendance-device-origin.model';

export interface IAttendanceTimeSheet {
  date?: dayjs.Dayjs;
  attendanceStatus?: AttendanceStatus;
  inTime?: dayjs.Dayjs;
  inNote?: string;
  outTime?: dayjs.Dayjs;
  outNote?: string;
  status?: Status;
  totalWorkingHour?: number;
  lateDuration?: number;
  hasPendingLeaveApplication?: boolean;
  hasPendingManualAttendance?: boolean;
  hasPendingMovementEntry?: boolean;
  hasPresentStatus?: boolean;
  hasMovementStatus?: boolean;
  hasLeaveStatus?: boolean;
  canApplyAttendanceEntry?: boolean;
  canEditAttendanceEntry?: boolean;
  canApplyMovementEntry?: boolean;
  canApplyLeaveApplication?: boolean;

  punchInDeviceOrigin?: AttendanceDeviceOrigin;
  punchOutDeviceOrigin?: AttendanceDeviceOrigin;
  isAutoPunchOut?: boolean;
}

export class AttendanceTimeSheet implements IAttendanceTimeSheet {
  constructor(
    public date?: dayjs.Dayjs,
    public attendanceStatus?: AttendanceStatus,
    public inTime?: dayjs.Dayjs,
    public inNote?: string,
    public outTime?: dayjs.Dayjs,
    public outNote?: string,
    public status?: Status,
    public totalWorkingHour?: number,
    public lateDuration?: number
  ) {}
}
