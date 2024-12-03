import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IBuilding } from 'app/entities/building/building.model';
import { IFloor } from 'app/entities/floor/floor.model';
import { IRoomType } from 'app/entities/room-type/room-type.model';

export interface IRoom {
  id: number;
  roomName?: string | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  capacity?: number | null;
  facilities?: string | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  building?: Pick<IBuilding, 'id'> | null;
  floor?: Pick<IFloor, 'id'> | null;
  roomType?: Pick<IRoomType, 'id'> | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
