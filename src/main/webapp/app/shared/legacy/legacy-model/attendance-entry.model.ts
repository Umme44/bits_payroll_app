import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';
import { AttendanceDeviceOrigin } from 'app/shared/model/enumerations/attendance-device-origin.model';

export interface IAttendanceEntry {
  id?: number;
  date?: dayjs.Dayjs;
  inTime?: dayjs.Dayjs;
  inNote?: string;
  outTime?: dayjs.Dayjs;
  outNote?: string;
  status?: Status;
  note?: string;
  punchInDeviceOrigin?: AttendanceDeviceOrigin;
  punchOutDeviceOrigin?: AttendanceDeviceOrigin;
  isAutoPunchOut?: boolean;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  bandName?: string;
}

export class AttendanceEntry implements IAttendanceEntry {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public inTime?: dayjs.Dayjs,
    public inNote?: string,
    public outTime?: dayjs.Dayjs,
    public outNote?: string,
    public status?: Status,
    public note?: string,
    public punchInDeviceOrigin?: AttendanceDeviceOrigin,
    public punchOutDeviceOrigin?: AttendanceDeviceOrigin,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public bandName?: string
  ) {}
}
