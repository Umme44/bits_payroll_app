import { IMonthlyAttendanceTimeSheet } from './monthly-attendance-time-sheet.model';

export interface IMonthlyAttendanceTimeSheetList {
  monthlyAttendanceTimeSheetList?: IMonthlyAttendanceTimeSheet[];
}

export class MonthlyAttendanceTimeSheetList implements IMonthlyAttendanceTimeSheetList {
  constructor(monthlyAttendanceTimeSheetList?: IMonthlyAttendanceTimeSheet[]) {}
}
