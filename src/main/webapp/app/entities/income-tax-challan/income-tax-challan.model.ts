import dayjs from 'dayjs/esm';
import { IAitConfig } from 'app/entities/ait-config/ait-config.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface IIncomeTaxChallan {
  id: number;
  challanNo?: string | null;
  challanDate?: dayjs.Dayjs | null;
  amount?: number | null;
  month?: Month | null;
  remarks?: string | null;
  aitConfig?: Pick<IAitConfig, 'id'> | null;
  aitConfigId?: number;
  year?: number | null;
  startYear?: number | null;
  endYear?: number | null;
}

export type NewIncomeTaxChallan = Omit<IIncomeTaxChallan, 'id'> & { id: null };
