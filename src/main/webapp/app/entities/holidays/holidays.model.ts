import dayjs from 'dayjs/esm';
import { HolidayType } from 'app/entities/enumerations/holiday-type.model';

export interface IHolidays {
  id: number;
  holidayType?: HolidayType | null;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  isMoonDependent?: boolean | null;
}

export type NewHolidays = Omit<IHolidays, 'id'> & { id: null };
