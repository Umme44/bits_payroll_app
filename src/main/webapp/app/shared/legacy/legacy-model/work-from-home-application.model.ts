import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';
import { EmployeeCategory } from '../../model/enumerations/employee-category.model';
import { IAttendance } from '../../../entities/attendance/attendance.model';

export interface IWorkFromHomeApplication {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  reason?: string;
  duration?: number;
  status?: Status;
  appliedAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  sanctionedAt?: dayjs.Dayjs;
  appliedByLogin?: string;
  appliedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  sanctionedByLogin?: string;
  sanctionedById?: number;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  bandName?: string;
  employeeCategory?: EmployeeCategory;
  isChecked?: boolean;
  isAllowedToGiveOnlineAttendance?: boolean;
  approvedStartDate?: dayjs.Dayjs;
}

export type WorkFromHomeApplication = Omit<IWorkFromHomeApplication, 'id'> & { id: null };

/*export class WorkFromHomeApplication implements IWorkFromHomeApplication {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public reason?: string,
    public duration?: number,
    public status?: Status,
    public appliedAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public sanctionedAt?: dayjs.Dayjs,
    public appliedByLogin?: string,
    public appliedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public sanctionedByLogin?: string,
    public sanctionedById?: number,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public bandName?: string,
    public employeeCategory?: EmployeeCategory,
    public isChecked?: boolean,
    public isAllowedToGiveOnlineAttendance?: boolean
  ) {}
}*/
