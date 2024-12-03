import { IEmployee } from '../legacy/legacy-model/employee.model';
import { IEmployeeSalary } from '../legacy/legacy-model/employee-salary.model';

export interface ISalaryPayslip {
  employee?: IEmployee;
  employeeSalary?: IEmployeeSalary;
}

export class SalaryPayslip implements ISalaryPayslip {
  constructor(public employee?: IEmployee, public employeeSalary?: IEmployeeSalary) {}
}
