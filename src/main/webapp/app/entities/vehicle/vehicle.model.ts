import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IVehicle {
  id: number;
  modelName?: string | null;
  chassisNumber?: string | null;
  registrationNumber?: string | null;
  status?: Status | null;
  capacity?: number | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  approvedAt?: dayjs.Dayjs | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  approvedBy?: Pick<IUser, 'id' | 'login'> | null;
  assignedDriver?: Pick<IEmployee, 'id'> | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
