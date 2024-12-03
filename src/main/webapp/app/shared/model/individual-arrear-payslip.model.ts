import dayjs from 'dayjs/esm';

export interface IIndividualArrearPayslip {
  id?: number;
  effectiveDate?: dayjs.Dayjs;
  title?: string;
  titleEffectiveFrom?: string;
  arrearRemarks?: string;

  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  joiningDate?: dayjs.Dayjs;
  bankName?: string;
  bankAccountNo?: string;

  basic?: number;
  houseRent?: number;
  medical?: number;
  conveyance?: number;

  grossPay?: number;
  festivalBonus?: number;
  livingAllowance?: number;
  otherAddition?: number;
  salaryAdjustment?: number;

  taxDeduction?: number;
  arrearPfDeduction?: number;

  totalAddition?: number;
  totalDeduction?: number;

  netPayable?: number;
}

export class IndividualArrearPayslip implements IIndividualArrearPayslip {
  constructor(
    public id?: number,
    public effectiveDate?: dayjs.Dayjs,
    public title?: string,
    public titleEffectiveFrom?: string,
    public arrearRemarks?: string,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public joiningDate?: dayjs.Dayjs,
    public bankName?: string,
    public bankAccountNo?: string,
    public basic?: number,
    public houseRent?: number,
    public medical?: number,
    public conveyance?: number,
    public grossPay?: number,
    public festivalBonus?: number,
    public livingAllowance?: number,
    public otherAddition?: number,
    public salaryAdjustment?: number,
    public taxDeduction?: number,
    public arrearPfDeduction?: number,
    public totalAddition?: number,
    public totalDeduction?: number,
    public netPayable?: number
  ) {}
}
