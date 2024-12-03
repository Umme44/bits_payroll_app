import { IAttendanceTimeSheetMini } from './attendance-time-sheet-mini.model';

export interface IMonthlyAttendanceTimeSheet {
  employeeId?: number;
  name?: String;
  pin?: number;
  attendanceTimeSheetMiniList?: IAttendanceTimeSheetMini[];
  isChecked?: Boolean;
}

export class MonthlyAttendanceTimeSheet implements IMonthlyAttendanceTimeSheet {
  constructor(
    employeeId?: number,
    name?: String,
    pin?: number,
    attendanceTimeSheetMiniList?: IAttendanceTimeSheetMini[],
    isChecked?: Boolean
  ) {}
}
