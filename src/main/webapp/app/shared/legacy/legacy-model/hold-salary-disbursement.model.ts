import dayjs from 'dayjs/esm';

export interface IHoldSalaryDisbursement {
  id?: number;
  date?: dayjs.Dayjs;
  userLogin?: string;
  userId?: number;
  employeeSalaryId?: number;

  salaryMonth?: number;
  salaryYear?: number;
  netPay?: number;
  totalDeduction?: number;
  otherDeduction?: number;
  employeeName?: number;
  pin?: number;
}

export class HoldSalaryDisbursement implements IHoldSalaryDisbursement {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public userLogin?: string,
    public userId?: number,
    public employeeSalaryId?: number,
    public salaryMonth?: number,
    public salaryYear?: number,
    public netPay?: number,
    public totalDeduction?: number,
    public otherDeduction?: number,
    public employeeName?: number,
    public pin?: number
  ) {}
}
