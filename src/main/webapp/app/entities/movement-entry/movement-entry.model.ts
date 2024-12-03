import dayjs from 'dayjs/esm';
import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IMovementEntry {
  id: number;
  startDate?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  startNote?: string | null;
  endDate?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  endNote?: string | null;
  type?: MovementType | null;
  status?: Status | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  sanctionAt?: dayjs.Dayjs | null;
  note?: string | null;

  // employee?: Pick<IEmployee, 'id'> | null;
  // createdByLogin?: Pick<IUser, 'id' | 'login'> | null;
  // updatedByLogin?: Pick<IUser, 'id' | 'login'> | null;
  // sanctionByLogin?: Pick<IUser, 'id' | 'login'> | null;
  // createdByLogin?: String| null;
  // updatedByLogin?: String| null;
  // sanctionByLogin?: String| null;

  employeeId?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  sanctionByLogin?: string;
  sanctionById?: number;

  employeeName?: string;
  pin?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;

  isChecked?: boolean;
}

export type NewMovementEntry = Omit<IMovementEntry, 'id'> & { id: null };

export class MovementEntry implements IMovementEntry {
  constructor(
    public id: number,
    public startDate?: dayjs.Dayjs | null,
    public startTime?: dayjs.Dayjs | null,
    public startNote?: string | null,
    public endDate?: dayjs.Dayjs | null,
    public endTime?: dayjs.Dayjs | null,
    public endNote?: string | null,
    public type?: MovementType | null,
    public status?: Status | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public sanctionAt?: dayjs.Dayjs | null,
    public note?: string | null,
    public employeeId?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public sanctionByLogin?: string,
    public sanctionById?: number,
    public employeeName?: string,
    public pin?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public isChecked?: boolean
  ) {}
}
