import dayjs from 'dayjs/esm';
import { HolidayType } from 'app/shared/model/enumerations/holiday-type.model';

export interface IHolidays {
  id?: number;
  holidayType?: HolidayType;
  description?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  isMoonDependent?: boolean;
  allDayNames?: string[];
}

export class Holidays implements IHolidays {
  constructor(
    public id?: number,
    public holidayType?: HolidayType,
    public description?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public isMoonDependent?: boolean
  ) {
    this.isMoonDependent = this.isMoonDependent || false;
  }
}
