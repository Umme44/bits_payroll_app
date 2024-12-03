import dayjs from 'dayjs/esm';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IPfLoanApplication {
  id?: number;
  installmentAmount?: number;
  noOfInstallment?: number;
  remarks?: string;
  isRecommended?: boolean;
  recommendationDate?: dayjs.Dayjs;
  isApproved?: boolean;
  approvalDate?: dayjs.Dayjs;
  isRejected?: boolean;
  rejectionReason?: string;
  rejectionDate?: dayjs.Dayjs;
  disbursementDate?: dayjs.Dayjs;
  disbursementAmount?: number;
  status?: Status;
  recommendedById?: number;
  approvedById?: number;
  approvedByFullName?: string;
  rejectedById?: number;
  rejectedByFullName?: number;
  pfAccountId?: number;

  pfCode?: string;
  accountStatus?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
}

export class PfLoanApplication implements IPfLoanApplication {
  constructor(
    public id?: number,
    public installmentAmount?: number,
    public noOfInstallment?: number,
    public remarks?: string,
    public isRecommended?: boolean,
    public recommendationDate?: dayjs.Dayjs,
    public isApproved?: boolean,
    public approvalDate?: dayjs.Dayjs,
    public isRejected?: boolean,
    public rejectionReason?: string,
    public rejectionDate?: dayjs.Dayjs,
    public disbursementDate?: dayjs.Dayjs,
    public disbursementAmount?: number,
    public status?: Status,
    public recommendedById?: number,
    public approvedById?: number,
    public approvedByFullName?: string,
    public rejectedById?: number,
    public rejectedByFullName?: number,
    public pfAccountId?: number,
    public pfCode?: string,
    public accountStatus?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public accHolderName?: string,
    public pin?: string
  ) {
    this.isRecommended = this.isRecommended || false;
    this.isApproved = this.isApproved || false;
    this.isRejected = this.isRejected || false;
  }
}
