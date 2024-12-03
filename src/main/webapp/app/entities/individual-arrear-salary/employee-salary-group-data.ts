import { Month } from 'app/shared/model/enumerations/month.model';

export interface IEmployeeSalaryGroupData {
  month?: Month;
  year?: number;
}
export class EmployeeSalaryGroupData implements IEmployeeSalaryGroupData {
  constructor(public month?: Month, public year?: number) {}
}
