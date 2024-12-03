export interface ITimeRangeAndEmployeeId {
  employeeId?: number;
  startDate?: number;
  endDate?: number;
}

export class TimeRangeAndEmployeeId implements ITimeRangeAndEmployeeId {
  constructor(public employeeId?: number, public startDate?: number, public endDate?: number) {}
}
