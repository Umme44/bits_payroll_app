import dayjs from 'dayjs/esm';

export interface Timeline {
  effectiveDate: dayjs.Dayjs;
  timelineName: string;
  note?: string;
}
