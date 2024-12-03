import dayjs from 'dayjs/esm';
import {Month} from 'app/entities/enumerations/month.model';
import {EmployeeCategory} from 'app/entities/enumerations/employee-category.model';
import {EmploymentStatus} from 'app/shared/model/enumerations/employment-status.model';

export interface IEmployeeSalary {
  id: number;
  month?: Month | null;
  year?: number | null;
  salaryGenerationDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  refPin?: string | null;
  pin?: string | null;
  joiningDate?: dayjs.Dayjs | null;
  confirmationDate?: dayjs.Dayjs | null;
  employeeCategory?: EmployeeCategory | null;
  unit?: string | null;
  department?: string | null;
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
  isFinalized?: boolean | null;
  isDispatched?: boolean | null;
  entertainment?: number | null;
  utility?: number | null;
  otherAddition?: number | null;
  salaryAdjustment?: number | null;
  providentFundArrear?: number | null;
  allowance01?: number | null;
  allowance02?: number | null;
  allowance03?: number | null;
  allowance04?: number | null;
  allowance05?: number | null;
  allowance06?: number | null;
  provisionForProjectBonus?: number | null;
  isHold?: boolean | null;
  attendanceRegularisationStartDate?: dayjs.Dayjs | null;
  attendanceRegularisationEndDate?: dayjs.Dayjs | null;
  title?: string | null;
  isVisibleToEmployee?: boolean | null;
  pfArrear?: number | null;
  taxCalculationSnapshot?: string | null;
  // employee?: Pick<IEmployee, 'id'> | null;

  employeeId?: number;
  employeeName?: string;
  designation?: string;
  employmentStatus?: EmploymentStatus;
  festivalBonus?: number;

  netPayInWords?: string;
  totalAddition?: number;
}

export type NewEmployeeSalary = Omit<IEmployeeSalary, 'id'> & { id: null };

export class EmployeeSalary implements IEmployeeSalary {
  constructor(
    public id: number,
    public month?: Month,
    public year?: number,
    public salaryGenerationDate?: dayjs.Dayjs,
    public createdBy?: string,
    public createdAt?: dayjs.Dayjs,
    public updatedBy?: string,
    public updatedAt?: dayjs.Dayjs,
    public refPin?: string,
    public pin?: string,
    public joiningDate?: dayjs.Dayjs,
    public confirmationDate?: dayjs.Dayjs,
    public employeeCategory?: EmployeeCategory,
    public unit?: string,
    public department?: string,
    public mainGrossSalary?: number,
    public mainGrossBasicSalary?: number,
    public mainGrossHouseRent?: number,
    public mainGrossMedicalAllowance?: number,
    public mainGrossConveyanceAllowance?: number,
    public absentDays?: number,
    public fractionDays?: number,
    public payableGrossSalary?: number,
    public payableGrossBasicSalary?: number,
    public payableGrossHouseRent?: number,
    public payableGrossMedicalAllowance?: number,
    public payableGrossConveyanceAllowance?: number,
    public arrearSalary?: number,
    public pfDeduction?: number,
    public taxDeduction?: number,
    public welfareFundDeduction?: number,
    public mobileBillDeduction?: number,
    public otherDeduction?: number,
    public totalDeduction?: number,
    public netPay?: number,
    public remarks?: string,
    public pfContribution?: number,
    public gfContribution?: number,
    public provisionForFestivalBonus?: number,
    public provisionForLeaveEncashment?: number,
    public isFinalized?: boolean,
    public isDispatched?: boolean,
    public entertainment?: number,
    public utility?: number,
    public otherAddition?: number,
    public salaryAdjustment?: number,
    public providentFundArrear?: number,
    public allowance01?: number,
    public allowance02?: number,
    public allowance03?: number,
    public allowance04?: number,
    public allowance05?: number,
    public allowance06?: number,
    public provisionForProjectBonus?: number,
    public isHold?: boolean,
    public attendanceRegularisationStartDate?: dayjs.Dayjs,
    public attendanceRegularisationEndDate?: dayjs.Dayjs,
    public title?: string,
    public isVisibleToEmployee?: boolean,
    public pfArrear?: number,
    public taxCalculationSnapshot?: any,
    public employeeId?: number,
    public employeeName?: string,
    public designation?: string,
    public employmentStatus?: EmploymentStatus,
    public festivalBonus?: number,
    public netPayInWords?: string,
    public totalAddition?: number,
  ) {
  }
}
