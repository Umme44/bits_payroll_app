export interface IAttendance {
  id?: number;
  year?: number;
  month?: number;
  absentDays?: number;
  fractionDays?: number;
  compensatoryLeaveGained?: number;
  employeeId?: number;
}

export class Attendance implements IAttendance {
  constructor(
    public id?: number,
    public year?: number,
    public month?: number,
    public absentDays?: number,
    public fractionDays?: number,
    public compensatoryLeaveGained?: number,
    public employeeId?: number
  ) {}
}
