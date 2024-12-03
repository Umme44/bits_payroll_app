import dayjs from 'dayjs/esm';

export interface IOffer {
  id: number;
  title?: string | null;
  description?: string | null;
  imagePath?: string | null;
  createdAt?: dayjs.Dayjs | null;
}

export type NewOffer = Omit<IOffer, 'id'> & { id: null };
