import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

export interface IEmployeePinConfiguration {
  id: number;
  employeeCategory?: EmployeeCategory | null;
  sequenceStart?: string | null;
  sequenceEnd?: string | null;
  lastSequence?: string | null;
  hasFullFilled?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  lastCreatedPin?: string | null;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;

  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmployeePinConfiguration = Omit<IEmployeePinConfiguration, 'id'> & { id: null };
