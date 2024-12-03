import dayjs from 'dayjs/esm';
import {Status} from 'app/entities/enumerations/status.model';
import {EmployeeCategory} from "../enumerations/employee-category.model";

export interface IWorkFromHomeApplication {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  reason?: string | null;
  duration?: number | null;
  status?: Status | null;
  appliedAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  sanctionedAt?: dayjs.Dayjs | null;

  // appliedBy?: Pick<IUser, 'id' | 'login'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // sanctionedBy?: Pick<IUser, 'id' | 'login'> | null;
  // employee?: Pick<IEmployee, 'id'> | null;

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
  approvedStartDate?: dayjs.Dayjs | null;
}

export type NewWorkFromHomeApplication = Omit<IWorkFromHomeApplication, 'id'> & { id: null };
