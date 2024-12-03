import dayjs from 'dayjs/esm';

export interface IFlexSchedule {
  id?: number;
  effectiveDate?: dayjs.Dayjs | null;
  inTime?: dayjs.Dayjs | null;
  outTime?: dayjs.Dayjs | null;
  // employee?: Pick<IEmployee, 'id'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  employeeId?: number;
  fullName?: string;
  pin?: string;
  createdByLogin?: string;
  createdById?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;

}

export type NewFlexSchedule = Omit<IFlexSchedule, 'id'> & { id: null };

export class FlexSchedule implements IFlexSchedule {
  constructor(
    public id?: number,
    public effectiveDate?: dayjs.Dayjs | null,
    public inTime?: dayjs.Dayjs | null,
    public outTime?: dayjs.Dayjs | null,
    public employeeId?: number,
    public fullName?: string,
    public pin?: string,
    public createdByLogin?: string,
    public createdById?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null
  ) {
  }
}
