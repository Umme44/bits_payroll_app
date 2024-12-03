import dayjs from 'dayjs/esm';

export interface ICalenderYear {
  id: number;
  year?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
}

export type NewCalenderYear = Omit<ICalenderYear, 'id'> & { id: null };
