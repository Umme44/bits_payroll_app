import dayjs from 'dayjs/esm';
import { NoticeStatus } from 'app/entities/enumerations/notice-status.model';

export interface IOfficeNotices {
  id: number;
  title?: string | null;
  description?: string | null;
  status?: NoticeStatus | null;
  publishForm?: dayjs.Dayjs | null;
  publishTo?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
}

export type NewOfficeNotices = Omit<IOfficeNotices, 'id'> & { id: null };
