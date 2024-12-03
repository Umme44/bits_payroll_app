import dayjs from 'dayjs/esm';

export interface ISpecialShiftTiming {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  overrideRoaster?: boolean | null;
  overrideFlexSchedule?: boolean | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  reason?: string | null;
  // timeSlot?: Pick<ITimeSlot, 'id'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;

  timeSlotId?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  inTime?: dayjs.Dayjs | null;
  outTime?: dayjs.Dayjs | null;
  timeSlotTitle?: string;

}

export type NewSpecialShiftTiming = Omit<ISpecialShiftTiming, 'id'> & { id: null };
