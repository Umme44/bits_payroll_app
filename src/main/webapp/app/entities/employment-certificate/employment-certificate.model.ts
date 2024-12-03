import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IUser } from 'app/entities/user/user.model';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';

export interface IEmploymentCertificate {
  id: number;
  certificateStatus?: CertificateStatus | null;
  referenceNumber?: string | null;
  issueDate?: dayjs.Dayjs | null;
  reason?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  generatedAt?: dayjs.Dayjs | null;
  employee?: Pick<IEmployee, 'id'> | null;
  signatoryPerson?: Pick<IEmployee, 'id'> | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  generatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmploymentCertificate = Omit<IEmploymentCertificate, 'id'> & { id: null };
