import { IEmployee } from 'app/entities/employee/employee.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface IEmployeeSalaryTempData {
  id: number;
  month?: Month | null;
  year?: number | null;
  mainGrossSalary?: number | null;
  mainGrossBasicSalary?: number | null;
  mainGrossHouseRent?: number | null;
  mainGrossMedicalAllowance?: number | null;
  mainGrossConveyanceAllowance?: number | null;
  absentDays?: number | null;
  fractionDays?: number | null;
  payableGrossSalary?: number | null;
  payableGrossBasicSalary?: number | null;
  payableGrossHouseRent?: number | null;
  payableGrossMedicalAllowance?: number | null;
  payableGrossConveyanceAllowance?: number | null;
  arrearSalary?: number | null;
  pfDeduction?: number | null;
  taxDeduction?: number | null;
  welfareFundDeduction?: number | null;
  mobileBillDeduction?: number | null;
  otherDeduction?: number | null;
  totalDeduction?: number | null;
  netPay?: number | null;
  remarks?: string | null;
  pfContribution?: number | null;
  gfContribution?: number | null;
  provisionForFestivalBonus?: number | null;
  provisionForLeaveEncashment?: number | null;
  provishionForProjectBonus?: number | null;
  livingAllowance?: number | null;
  otherAddition?: number | null;
  salaryAdjustment?: number | null;
  providentFundArrear?: number | null;
  entertainment?: number | null;
  utility?: number | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewEmployeeSalaryTempData = Omit<IEmployeeSalaryTempData, 'id'> & { id: null };
