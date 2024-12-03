import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IRoomType {
  id: number;
  typeName?: string | null;
  remarks?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewRoomType = Omit<IRoomType, 'id'> & { id: null };
