import dayjs from 'dayjs/esm';
import { IDepartment } from 'app/entities/department/department.model';
import { IUnitOfMeasurement } from 'app/entities/unit-of-measurement/unit-of-measurement.model';
import { IUser } from 'app/entities/user/user.model';

export interface IItemInformation {
  id: number;
  code?: string | null;
  name?: string | null;
  specification?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;

  /* department?: Pick<IDepartment, 'id' | 'departmentName'> | null;
  unitOfMeasurement?: Pick<IUnitOfMeasurement, 'id' | 'name'> | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;*/

  departmentName?: string;
  departmentId?: number;
  unitOfMeasurementName?: string;
  unitOfMeasurementId?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  disabled?: boolean;
}

export type NewItemInformation = Omit<IItemInformation, 'id'> & { id: null };
