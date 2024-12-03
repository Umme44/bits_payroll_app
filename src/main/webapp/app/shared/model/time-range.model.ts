export interface ITimeRange {
  startDate?: number;
  endDate?: number;
}

export class TimeRange implements ITimeRange {
  constructor(public startDate?: number, public endDate?: number) {}
}
