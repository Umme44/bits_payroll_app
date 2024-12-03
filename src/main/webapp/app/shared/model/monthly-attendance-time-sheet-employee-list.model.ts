import dayjs from 'dayjs/esm';

export class IMonthlyAttendanceTimeSheetEmployeeList {
  employeeIdList?: (number | undefined)[];
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
}
