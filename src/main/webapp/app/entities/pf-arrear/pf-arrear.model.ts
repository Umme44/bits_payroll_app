import { IEmployee } from 'app/entities/employee/employee.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface IPfArrear {
  id: number;
  month?: Month | null;
  year?: number | null;
  amount?: number | null;
  remarks?: string | null;
  employee?: Pick<IEmployee, 'id'> | null;
  employeeId?: number | null;

  pin?: string | null;
  fullName?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
}

export type NewPfArrear = Omit<IPfArrear, 'id'> & { id: null };
