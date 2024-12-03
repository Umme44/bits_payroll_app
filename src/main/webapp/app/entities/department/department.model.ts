import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDepartment {
  id: number;
  departmentName?: string | null;
  departmentHeadId?: number;
  departmentHeadFullName?: string;
  departmentHeadPin?: string;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
