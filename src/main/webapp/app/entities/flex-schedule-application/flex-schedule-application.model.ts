import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';

export interface IFlexScheduleApplication {
  id: number;
  effectiveFrom?: dayjs.Dayjs | null;
  effectiveTo?: dayjs.Dayjs | null;
  reason?: string | null;
  status?: Status | null;
  sanctionedAt?: dayjs.Dayjs | null;
  appliedAt?: dayjs.Dayjs | null;
  durationAsDays?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  requesterId?: number | null;
  fullName?: string | null;
  pin?: string | null;
  designationName?: string | null;
  sanctionedByLogin?: string;
  sanctionedById?: number;
  appliedByLogin?: string;
  appliedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  timeSlotId?: number;
  inTime?: dayjs.Dayjs | null;
  outTime?: dayjs.Dayjs | null;
  timeSlotList?: string[];
  // requester?: Pick<IEmployee, 'id'> | null;
  // sanctionedBy?: Pick<IUser, 'id' | 'login'> | null;
  // appliedBy?: Pick<IUser, 'id' | 'login'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // timeSlot?: Pick<ITimeSlot, 'id'> | null;
}

export type NewFlexScheduleApplication = Omit<IFlexScheduleApplication, 'id'> & { id: null };
