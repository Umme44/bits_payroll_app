import dayjs from 'dayjs/esm';
import { MovementType } from 'app/shared/model/enumerations/movement-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IMovementEntry {
  id?: number;
  startDate?: dayjs.Dayjs;
  startTime?: dayjs.Dayjs;
  startNote?: string;
  endDate?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  endNote?: string;
  type?: MovementType;
  status?: Status;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  sanctionAt?: dayjs.Dayjs;
  note?: string;
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

export class MovementEntry implements IMovementEntry {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs,
    public startTime?: dayjs.Dayjs,
    public startNote?: string,
    public endDate?: dayjs.Dayjs,
    public endTime?: dayjs.Dayjs,
    public endNote?: string,
    public type?: MovementType,
    public status?: Status,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public sanctionAt?: dayjs.Dayjs,
    public note?: string,
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
