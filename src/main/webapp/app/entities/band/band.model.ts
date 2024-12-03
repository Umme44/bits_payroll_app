import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IBand {
  id: number;
  bandName?: string | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  welfareFund?: number | null;
  mobileCelling?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewBand = Omit<IBand, 'id'> & { id: null };
