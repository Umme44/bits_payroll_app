import dayjs from 'dayjs/esm';

export interface IFraction {
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  effectiveGross?: number;
  daysBetween?: number;
  perDayMainGross?: number;
}

export class Fraction implements Fraction {
  constructor(
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public effectiveGross?: number,
    public daysBetween?: number,
    public perDayMainGross?: number
  ) {}
}
