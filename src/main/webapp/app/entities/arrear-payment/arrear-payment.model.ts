import dayjs from 'dayjs/esm';
import { IArrearSalaryItem } from 'app/entities/arrear-salary-item/arrear-salary-item.model';
import { ArrearPaymentType } from 'app/entities/enumerations/arrear-payment-type.model';
import { Month } from 'app/entities/enumerations/month.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IArrearPayment {
  id: number;
  paymentType?: ArrearPaymentType | null;
  disbursementDate?: dayjs.Dayjs | null;
  salaryMonth?: Month | null;
  salaryYear?: number | null;
  approvalStatus?: Status | null;
  disbursementAmount?: number | null;
  isDeleted?: boolean | null;
  arrearPF?: number | null;
  taxDeduction?: number | null;
  deductTaxUponPayment?: boolean | null;
  arrearSalaryItem?: Pick<IArrearSalaryItem, 'id'> | null;
}

export type NewArrearPayment = Omit<IArrearPayment, 'id'> & { id: null };
