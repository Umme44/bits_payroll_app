import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IEmployeeResignation {
  id: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  resignationDate?: dayjs.Dayjs | null;
  approvalStatus?: Status | null;
  approvalComment?: string | null;
  isSalaryHold?: boolean | null;
  isFestivalBonusHold?: boolean | null;
  resignationReason?: string | null;
  lastWorkingDay?: dayjs.Dayjs | null;

  // createdBy?: Pick<IEmployee, 'id'> | null;
  // uodatedBy?: Pick<IEmployee, 'id'> | null;
  // employee?: Pick<IEmployee, 'id'> | null;

  createdById?: number;
  uodatedById?: number;
  employeeId?: number;
  pin?: string;
  name?: string;
  designation?: string;
  unit?: string;
  department?: string;
}

export type NewEmployeeResignation = Omit<IEmployeeResignation, 'id'> & { id: null };
