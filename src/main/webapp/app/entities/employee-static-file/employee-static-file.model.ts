import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEmployeeStaticFile {
  id: number;
  filePath?: string | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewEmployeeStaticFile = Omit<IEmployeeStaticFile, 'id'> & { id: null };
