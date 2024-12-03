import { IEmployee } from 'app/entities/employee/employee.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface IArrearSalary {
  id: number;
  month?: Month | null;
  year?: number | null;
  amount?: number | null;
  arrearType?: string | null;
  employee?: Pick<IEmployee, 'id'> | null;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export type NewArrearSalary = Omit<IArrearSalary, 'id'> & { id: null };
