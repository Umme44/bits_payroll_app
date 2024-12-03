import { IArrearSalaryMaster } from 'app/entities/arrear-salary-master/arrear-salary-master.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IArrearSalaryItem {
  id: number;
  title?: string | null;
  arrearAmount?: number | null;
  hasPfArrearDeduction?: boolean | null;
  pfArrearDeduction?: number | null;
  isFestivalBonus?: boolean | null;
  isDeleted?: boolean | null;
  arrearSalaryMaster?: Pick<IArrearSalaryMaster, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewArrearSalaryItem = Omit<IArrearSalaryItem, 'id'> & { id: null };
