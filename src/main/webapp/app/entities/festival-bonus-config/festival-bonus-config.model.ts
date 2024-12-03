import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

export interface IFestivalBonusConfig {
  id: number;
  employeeCategory?: EmployeeCategory | null;
  percentageFromGross?: number | null;
}

export type NewFestivalBonusConfig = Omit<IFestivalBonusConfig, 'id'> & { id: null };
