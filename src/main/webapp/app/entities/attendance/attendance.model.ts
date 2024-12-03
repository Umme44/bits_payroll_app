import { IEmployee } from 'app/entities/employee/employee.model';

export interface IAttendance {
  id: number;
  year?: number | null;
  month?: number | null;
  absentDays?: number | null;
  fractionDays?: number | null;
  compensatoryLeaveGained?: number | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewAttendance = Omit<IAttendance, 'id'> & { id: null };
