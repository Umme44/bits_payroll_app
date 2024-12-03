import { EmployeeCategory } from '../../enumerations/employee-category.model';
import { PfLoanStatus } from '../../enumerations/pf-loan-status.model';
import { PfAccountStatus } from '../../enumerations/pf-account-status.model';
import dayjs from 'dayjs/esm'
import { Status } from '../../enumerations/status.model';

export interface IPfLoanApplicationForm {
  pfLoanApplicationId?: number;
  dateOfJoining?: dayjs.Dayjs;
  pfContribution?: number;
  installmentAmount?: number;
  noOfInstallment?: number;
  instalmentStartFrom?: dayjs.Dayjs;
  remarks?: string;
  isRecommended?: boolean;
  recommendationDate?: dayjs.Dayjs;
  isApproved?: boolean;
  approvalDate?: dayjs.Dayjs;
  isRejected?: boolean;
  rejectionReason?: string;
  rejectionDate?: dayjs.Dayjs;
  disbursementAmount?: number;
  disbursementDate?: dayjs.Dayjs;
  status?: Status;
  bankName?: string;
  bankBranch?: string;
  bankAccountNumber?: string;
  chequeNumber?: string;
  recommendedById?: number;
  approvedById?: number;
  rejectedById?: number;
  pfAccountId?: number;
  pfCode?: string;
  accountStatus?: PfAccountStatus;
  pfLoanStatus?: PfLoanStatus;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
  employeeCategory?: EmployeeCategory;
  regularConfirmedEmployee?: boolean;
  eligibleBand?: boolean;
  bandName?: number;
  pfAccountMatured?: boolean;
  memberShipTotalDays?: number;
  anyOpenRepayingPfLoan?: boolean;
  pfLoanEligibleAmount?: number;
}

export class PfLoanApplicationForm implements IPfLoanApplicationForm {
  constructor(
    public pfLoanApplicationId?: number,
    public dateOfJoining?: dayjs.Dayjs,
    public pfContribution?: number,
    public installmentAmount?: number,
    public noOfInstallment?: number,
    public instalmentStartFrom?: dayjs.Dayjs,
    public remarks?: string,
    public isRecommended?: boolean,
    public recommendationDate?: dayjs.Dayjs,
    public isApproved?: boolean,
    public approvalDate?: dayjs.Dayjs,
    public isRejected?: boolean,
    public rejectionReason?: string,
    public rejectionDate?: dayjs.Dayjs,
    public disbursementAmount?: number,
    public disbursementDate?: dayjs.Dayjs,
    public status?: Status,
    public bankName?: string,
    public bankBranch?: string,
    public bankAccountNumber?: string,
    public chequeNumber?: string,
    public recommendedById?: number,
    public approvedById?: number,
    public rejectedById?: number,
    public pfAccountId?: number,
    public pfCode?: string,
    public accountStatus?: PfAccountStatus,
    public pfLoanStatus?: PfLoanStatus,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public accHolderName?: string,
    public pin?: string,
    public employeeCategory?: EmployeeCategory,
    public regularConfirmedEmployee?: boolean,
    public eligibleBand?: boolean,
    public bandName?: number,
    public pfAccountMatured?: boolean,
    public memberShipTotalDays?: number,
    public anyOpenRepayingPfLoan?: boolean,
    public pfLoanEligibleAmount?: number
  ) {}
}
