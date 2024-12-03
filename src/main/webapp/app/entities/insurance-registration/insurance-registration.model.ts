import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IUser } from 'app/entities/user/user.model';
import { InsuranceStatus } from 'app/entities/enumerations/insurance-status.model';
import { InsuranceRelation } from '../../shared/model/enumerations/insurance-relation.model';

export interface IInsuranceRegistration {
  id: number;
  name?: string | null;
  age?: number | null;
  dateOfBirth?: dayjs.Dayjs | null;
  photo?: string | null;
  insuranceId?: string | null;
  insuranceRelation?: InsuranceRelation | null;
  insuranceStatus?: InsuranceStatus | null;
  unapprovalReason?: string | null;
  availableBalance?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  approvedAt?: dayjs.Dayjs | null;

  // employee?: Pick<IEmployee, 'id'> | null;
  // approvedBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;

  employeeId?: number;
  employeePin?: string;
  employeeName?: string;
  approvedByLogin?: string;
  approvedById?: number;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  effectiveDate?: dayjs.Dayjs | null;
}

export type NewInsuranceRegistration = Omit<IInsuranceRegistration, 'id'> & { id: null };
