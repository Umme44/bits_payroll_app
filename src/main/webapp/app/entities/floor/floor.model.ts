import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IBuilding } from 'app/entities/building/building.model';

export interface IFloor {
  id: number;
  floorName?: string | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  building?: Pick<IBuilding, 'id'> | null;
}

export type NewFloor = Omit<IFloor, 'id'> & { id: null };
