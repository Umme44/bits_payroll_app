import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IAitPayment {
  id?: number;
  date?: dayjs.Dayjs | null;
  amount?: number | null;
  description?: string | null;
  employee?: Pick<IEmployee, 'id'> | null;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export type NewAitPayment = Omit<IAitPayment, 'id'> & { id: null };
