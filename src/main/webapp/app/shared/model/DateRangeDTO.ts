import dayjs from 'dayjs/esm';

export interface IDateRangeDTO {
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
}

export class DateRangeDTO implements IDateRangeDTO {
  constructor(public startDate?: dayjs.Dayjs, public endDate?: dayjs.Dayjs) {}
}
