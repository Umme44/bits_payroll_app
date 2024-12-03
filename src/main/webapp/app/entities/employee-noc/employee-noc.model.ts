import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IUser } from 'app/entities/user/user.model';
import { PurposeOfNOC } from 'app/entities/enumerations/purpose-of-noc.model';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

export interface IEmployeeNOC {
  id: number;
  passportNumber?: string | null;
  leaveStartDate?: dayjs.Dayjs | null;
  leaveEndDate?: dayjs.Dayjs | null;
  countryToVisit?: string | null;
  referenceNumber?: string | null;
  issueDate?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  generatedAt?: dayjs.Dayjs | null;
  reason?: string | null;
  purposeOfNOC?: PurposeOfNOC | null;
  certificateStatus?: CertificateStatus | null;
  isRequiredForVisa?: boolean | null;
  employee?: Pick<IEmployee, 'id'> | null;
  signatoryPerson?: Pick<IEmployee, 'id'> | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  generatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmployeeNOC = Omit<IEmployeeNOC, 'id'> & { id: null };
