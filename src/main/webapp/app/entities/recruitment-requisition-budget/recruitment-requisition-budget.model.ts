import { IEmployee } from 'app/entities/employee/employee.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IRecruitmentRequisitionBudget {
  id: number;
  year?: number | null;
  budget?: number | null;
  remainingBudget?: number | null;
  remainingManpower?: number | null;
  employeeId?: number;
  employeeFullName?: string;
  employeePin?: string;
  departmentId?: number;
  departmentName?: string;
}

export type NewRecruitmentRequisitionBudget = Omit<IRecruitmentRequisitionBudget, 'id'> & { id: null };
