import dayjs from 'dayjs/esm';
import { InsuranceRelation } from 'app/shared/model/enumerations/insurance-relation.model';
import { InsuranceStatus } from 'app/shared/model/enumerations/insurance-status.model';

export interface IInsuranceRegistration {
  id?: number;
  name?: string;
  dateOfBirth?: dayjs.Dayjs;
  photo?: any;
  insuranceId?: string;
  insuranceRelation?: InsuranceRelation;
  insuranceStatus?: InsuranceStatus;
  unapprovalReason?: string;
  availableBalance?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  approvedAt?: dayjs.Dayjs;
  employeeId?: number;
  employeePin?: string;
  employeeName?: string;
  approvedByLogin?: string;
  approvedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
}

export class InsuranceRegistration implements IInsuranceRegistration {
  constructor(
    public id?: number,
    public name?: string,
    public dateOfBirth?: dayjs.Dayjs,
    public photo?: any,
    public insuranceId?: string,
    public insuranceRelation?: InsuranceRelation,
    public insuranceStatus?: InsuranceStatus,
    public unapprovalReason?: string,
    public availableBalance?: number,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public approvedAt?: dayjs.Dayjs,
    public employeeId?: number,
    public employeePin?: string,
    public employeeName?: string,
    public approvedByLogin?: string,
    public approvedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number
  ) {}
}
