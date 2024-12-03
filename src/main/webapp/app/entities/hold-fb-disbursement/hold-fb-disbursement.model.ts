import dayjs from 'dayjs/esm';

export interface IHoldFbDisbursement {
  id: number;
  disbursedAt?: dayjs.Dayjs | null;
  remarks?: string | null;
  disbursedByLogin?: string | null;
  disbursedById?: number;
  festivalBonusDetailId?: number;
  employeeName?: string | null;
  pin?: string | null;
  bonusAmount?: number | null;
  festivalTitle?: string | null;
  festivalName?: string | null;
}

export type NewHoldFbDisbursement = Omit<IHoldFbDisbursement, 'id'> & { id: null };
