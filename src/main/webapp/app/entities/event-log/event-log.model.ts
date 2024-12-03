import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { RequestMethod } from 'app/entities/enumerations/request-method.model';

export interface IEventLog {
  id: number;
  title?: string | null;
  requestMethod?: RequestMethod | null;
  performedAt?: dayjs.Dayjs | null;
  data?: string | null;
  entityName?: string | null;
  performedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEventLog = Omit<IEventLog, 'id'> & { id: null };
