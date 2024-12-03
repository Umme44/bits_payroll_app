import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IVehicleRequisition {
  id: number;
  purpose?: string | null;
  otherPassengersName?: string | null;
  totalNumberOfPassengers?: number | null;
  status?: Status | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  sanctionAt?: dayjs.Dayjs | null;
  transactionNumber?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  startTime?: number | null;
  endTime?: number | null;
  area?: string | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  approvedBy?: Pick<IUser, 'id' | 'login'> | null;
  requester?: Pick<IEmployee, 'id'> | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
}

export type NewVehicleRequisition = Omit<IVehicleRequisition, 'id'> & { id: null };
