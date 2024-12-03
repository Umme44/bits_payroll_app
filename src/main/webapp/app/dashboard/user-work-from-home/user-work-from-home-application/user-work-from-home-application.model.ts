import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';
import { EmployeeCategory } from '../../../entities/enumerations/employee-category.model';

export interface IUserWorkFromHomeApplication {
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

export type UserWorkFromHomeApplication = Omit<IUserWorkFromHomeApplication, 'id'> & { id: null };
