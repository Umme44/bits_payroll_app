import dayjs from 'dayjs/esm';

export interface ITimeSlot {
  id?: number;
  title?: string;
  inTime?: dayjs.Dayjs;
  outTime?: dayjs.Dayjs;
  isApplicableByEmployee?: boolean;
  isDefaultShift?: boolean;
  code?: string;
  weekEnds?: any;
  weekEndList?: string[];
}

export class TimeSlot implements ITimeSlot {
  constructor(
    public id?: number,
    public title?: string,
    public inTime?: dayjs.Dayjs,
    public outTime?: dayjs.Dayjs,
    public isApplicableByEmployee?: boolean,
    public isDefaultShift?: boolean,
    public code?: string,
    public weekEnds?: any,
    public weekEndList?: string[]
  ) {
    this.isApplicableByEmployee = this.isApplicableByEmployee || false;
    this.isDefaultShift = this.isDefaultShift || false;
  }
}
