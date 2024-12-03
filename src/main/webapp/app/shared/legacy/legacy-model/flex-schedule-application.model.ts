import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IFlexScheduleApplication {
  id?: number;
  effectiveFrom?: dayjs.Dayjs;
  effectiveTo?: dayjs.Dayjs;
  reason?: any;
  status?: Status;
  sanctionedAt?: dayjs.Dayjs;
  appliedAt?: dayjs.Dayjs;
  durationAsDays?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  requesterId?: number;
  fullName?: string;
  pin?: string;
  designationName?: string;
  sanctionedByLogin?: string;
  sanctionedById?: number;
  appliedByLogin?: string;
  appliedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  timeSlotId?: number;
  inTime?: dayjs.Dayjs;
  outTime?: dayjs.Dayjs;
  timeSlotList?: string[];
}

export class FlexScheduleApplication implements IFlexScheduleApplication {
  constructor(
    public id?: number,
    public effectiveFrom?: dayjs.Dayjs,
    public effectiveTo?: dayjs.Dayjs,
    public reason?: any,
    public status?: Status,
    public sanctionedAt?: dayjs.Dayjs,
    public appliedAt?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public requesterId?: number,
    public sanctionedByLogin?: string,
    public sanctionedById?: number,
    public appliedByLogin?: string,
    public appliedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public timeSlotId?: number,
    public timeSlotList?: string[]
  ) {}
}
