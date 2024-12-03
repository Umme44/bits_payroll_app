import dayjs from 'dayjs/esm';
import { Month } from 'app/shared/model/enumerations/month.model';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';

export interface IEmployeeSalary {
  id?: number;
  month?: Month;
  year?: number;
  salaryGenerationDate?: dayjs.Dayjs;
  createdBy?: string;
  createdAt?: dayjs.Dayjs;
  updatedBy?: string;
  updatedAt?: dayjs.Dayjs;
  refPin?: string;
  pin?: string;
  joiningDate?: dayjs.Dayjs;
  confirmationDate?: dayjs.Dayjs;
  employeeCategory?: EmployeeCategory;
  unit?: string;
  department?: string;
  mainGrossSalary?: number;
  mainGrossBasicSalary?: number;
  mainGrossHouseRent?: number;
  mainGrossMedicalAllowance?: number;
  mainGrossConveyanceAllowance?: number;
  absentDays?: number;
  fractionDays?: number;
  payableGrossSalary?: number;
  payableGrossBasicSalary?: number;
  payableGrossHouseRent?: number;
  payableGrossMedicalAllowance?: number;
  payableGrossConveyanceAllowance?: number;
  arrearSalary?: number;
  pfDeduction?: number;
  taxDeduction?: number;
  welfareFundDeduction?: number;
  mobileBillDeduction?: number;
  otherDeduction?: number;
  totalDeduction?: number;
  netPay?: number;
  remarks?: string;
  pfContribution?: number;
  gfContribution?: number;
  provisionForFestivalBonus?: number;
  provisionForLeaveEncashment?: number;
  isFinalized?: boolean;
  isDispatched?: boolean;
  entertainment?: number;
  utility?: number;
  otherAddition?: number;
  salaryAdjustment?: number;
  providentFundArrear?: number;
  allowance01?: number;
  allowance02?: number;
  allowance03?: number;
  allowance04?: number;
  allowance05?: number;
  allowance06?: number;
  provisionForProjectBonus?: number;
  isHold?: boolean;
  attendanceRegularisationStartDate?: dayjs.Dayjs;
  attendanceRegularisationEndDate?: dayjs.Dayjs;
  title?: string;
  isVisibleToEmployee?: boolean;
  pfArrear?: number;
  taxCalculationSnapshot?: any;
  employeeId?: number;
  employeeName?: string;
  designation?: string;
  employmentStatus?: EmploymentStatus;
  festivalBonus?: number;

  netPayInWords?: string;
  totalAddition?: number;
}

export class EmployeeSalary implements IEmployeeSalary {
  constructor(
    public id?: number,
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
    public totalAddition?: number
  ) {
    this.isFinalized = this.isFinalized || false;
    this.isDispatched = this.isDispatched || false;
    this.isHold = this.isHold || false;
    this.isVisibleToEmployee = this.isVisibleToEmployee || false;
  }
}
