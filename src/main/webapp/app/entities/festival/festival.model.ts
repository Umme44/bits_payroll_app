import dayjs from 'dayjs/esm';
import { Religion } from 'app/entities/enumerations/religion.model';

export interface IFestival {
  id: number;
  title?: string | null;
  festivalName?: string | null;
  festivalDate?: dayjs.Dayjs | null;
  bonusDisbursementDate?: dayjs.Dayjs | null;
  religion?: Religion | null;
  isProRata?: boolean | null;
  numberOfBonus?: boolean;
}

export type NewFestival = Omit<IFestival, 'id'> & { id: null };
