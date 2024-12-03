import dayjs from 'dayjs/esm';

export interface ITimeSlot {
  id: number;
  title?: string | null;
  inTime?: dayjs.Dayjs | null;
  outTime?: dayjs.Dayjs | null;
  isApplicableByEmployee?: boolean | null;
  isDefaultShift?: boolean | null;
  code?: string | null;
  weekEnds?: string | null;
  weekEndList?: string[];
}

export type NewTimeSlot = Omit<ITimeSlot, 'id'> & { id: null };
