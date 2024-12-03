import dayjs from 'dayjs/esm';
import { Month } from 'app/shared/model/enumerations/month.model';

export interface IIncomeTaxChallan {
  id?: number;
  challanNo?: string;
  challanDate?: dayjs.Dayjs;
  amount?: number;
  month?: Month;
  remarks?: string;
  aitConfigId?: number;
  year?: number;
  startYear?: number;
  endYear?: number;
}

export class IncomeTaxChallan implements IIncomeTaxChallan {
  constructor(
    public id?: number,
    public challanNo?: string,
    public challanDate?: dayjs.Dayjs,
    public amount?: number,
    public month?: Month,
    public remarks?: string,
    public aitConfigId?: number,
    public year?: number,
    public startYear?: number,
    public endYear?: number
  ) {}
}
