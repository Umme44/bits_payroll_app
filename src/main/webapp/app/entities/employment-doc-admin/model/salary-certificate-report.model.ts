import { Month } from 'app/shared/model/enumerations/month.model';
import dayjs from 'dayjs/esm';
import { EmployeeCategory } from '../../../shared/model/enumerations/employee-category.model';

export interface ISalaryCertificateReport {
  employeeName?: string;
  employeeLastName?: string;
  salutation?: string;
  pin?: string;
  joiningDate?: dayjs.Dayjs;
  confirmationDate?: dayjs.Dayjs;
  employeeCategory?: EmployeeCategory;
  designation?: string;
  unit?: string;
  department?: string;
  month?: Month;
  year?: number;

  payableGrossBasicSalary?: number;
  payableGrossHouseRent?: number;
  payableGrossMedicalAllowance?: number;
  payableGrossConveyanceAllowance?: number;
  livingAllowance?: number;

  entertainment?: number;
  utility?: number;
  otherAddition?: number;
  payableGrossSalary?: number;

  pfDeduction?: number;
  taxDeduction?: number;
  mobileBillDeduction?: number;
  welfareFundDeduction?: number;
  otherDeduction?: number;
  totalDeduction?: number;

  netPay?: number;
  netPayInWords?: string;

  signatoryPersonId?: number;
  signatoryPersonName?: string;
  signatoryPersonDesignation?: string;

  referenceNumber?: string;
}

export class SalaryCertificateReport implements ISalaryCertificateReport {
  constructor(
    public employeeName?: string,
    public employeeLastName?: string,
    public salutation?: string,
    public pin?: string,
    public joiningDate?: dayjs.Dayjs,
    public confirmationDate?: dayjs.Dayjs,
    public employeeCategory?: EmployeeCategory,
    public designation?: string,
    public unit?: string,
    public department?: string,
    public month?: Month,
    public year?: number,

    public payableGrossBasicSalary?: number,
    public payableGrossHouseRent?: number,
    public payableGrossMedicalAllowance?: number,
    public payableGrossConveyanceAllowance?: number,
    public livingAllowance?: number,

    public entertainment?: number,
    public utility?: number,
    public otherAddition?: number,
    public payableGrossSalary?: number,

    public pfDeduction?: number,
    public taxDeduction?: number,
    public mobileBillDeduction?: number,
    public welfareFundDeduction?: number,
    public otherDeduction?: number,
    public totalDeduction?: number,

    public netPay?: number,
    public netPayInWords?: string,

    public signatoryPersonId?: number,
    public signatoryPersonName?: string,
    public signatoryPersonDesignation?: string,
    public referenceNumber?: string
  ) {}
}
