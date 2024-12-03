import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IWorkingExperience {
  id: number;
  lastDesignation?: string | null;
  organizationName?: string | null;
  dojOfLastOrganization?: dayjs.Dayjs | null;
  dorOfLastOrganization?: dayjs.Dayjs | null;
  employeeId?: number;
  pin?: number;
  employeeName?: number;
  designationName?: number;
  departmentName?: number;
  unitName?: number;
}

export type NewWorkingExperience = Omit<IWorkingExperience, 'id'> & { id: null };
