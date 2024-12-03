import dayjs from 'dayjs/esm';

export interface IAitConfig {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  taxConfig?: string | null;
  startYear?: number | null;
  endYear?: number | null;
}

export type NewAitConfig = Omit<IAitConfig, 'id'> & { id: null };
