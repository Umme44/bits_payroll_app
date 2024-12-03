import { PurposeOfNOC } from 'app/entities/enumerations/purpose-of-noc.model';
import { CertificateStatus } from 'app/entities/enumerations/certificate-status.model';
import dayjs from 'dayjs/esm';
import { Gender } from '../../enumerations/gender.model';

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

  employeeId?: number;
  employeeName?: string;
  employeeSurName?: string;
  employeePin?: string;
  departmentName?: string;
  designationName?: string;
  unitName?: string;
  dateOfJoining?: dayjs.Dayjs | null;
  employeeGender?: Gender;

  signatoryPersonId?: number;
  signatoryPersonPin?: string;
  signatoryPersonName?: number;
  signatoryPersonDepartment?: number;
  signatoryPersonDesignation?: number;
  signatoryPersonUnit?: number;
  signatoryPersonEmail?: string;
  signatoryPersonCell?: string;

  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  generatedByLogin?: string;
  generatedById?: number;

  // employee?: Pick<IEmployee, 'id'> | null;
  // signatoryPerson?: Pick<IEmployee, 'id'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // generatedBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmployeeNOC = Omit<IEmployeeNOC, 'id'> & { id: null };
