import dayjs from 'dayjs/esm';
import { NoticeStatus } from 'app/shared/model/enumerations/notice-status.model';

export interface IOfficeNotices {
  id?: number;
  title?: string;
  description?: any;
  status?: NoticeStatus;
  publishForm?: dayjs.Dayjs;
  publishTo?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  createdBy?: string;
  updatedBy?: string;
}

export class OfficeNotices implements IOfficeNotices {
  constructor(
    public id?: number,
    public title?: string,
    public description?: any,
    public status?: NoticeStatus,
    public publishForm?: dayjs.Dayjs,
    public publishTo?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public createdBy?: string,
    public updatedBy?: string
  ) {}
}
