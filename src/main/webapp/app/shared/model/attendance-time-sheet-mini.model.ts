import dayjs from 'dayjs/esm';
import { AttendanceStatus } from './enumerations/attendance-status.model';

export interface IAttendanceTimeSheetMini {
  date?: dayjs.Dayjs;
  attendanceStatus?: AttendanceStatus;
}

export class AttendanceTimeSheet implements IAttendanceTimeSheetMini {
  constructor(public date?: dayjs.Dayjs, public attendanceStatus?: AttendanceStatus) {}
}
