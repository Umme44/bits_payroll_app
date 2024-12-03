import { CertificateStatus } from 'app/shared/model/enumerations/certificate-status.model';
import dayjs from 'dayjs/esm';
import { Gender } from '../../model/enumerations/gender.model';

export interface IEmploymentCertificate {
  id?: number;
  certificateStatus?: CertificateStatus;
  referenceNumber?: string;
  issueDate?: dayjs.Dayjs;
  reason?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  generatedAt?: dayjs.Dayjs;

  employeeId?: number;
  employeeName?: string;
  employeePin?: string;
  departmentName?: string;
  designationName?: string;
  unitName?: string;
  dateOfJoining?: dayjs.Dayjs;
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
}

export class EmploymentCertificate implements IEmploymentCertificate {
  constructor(
    public id?: number,
    public certificateStatus?: CertificateStatus,
    public referenceNumber?: string,
    public issueDate?: dayjs.Dayjs,
    public reason?: string,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public generatedAt?: dayjs.Dayjs,
    public employeeId?: number,
    public employeeName?: string,
    public employeePin?: string,
    public departmentName?: string,
    public designationName?: string,
    public unitName?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public employeeGender?: Gender,

    public signatoryPersonId?: number,
    public signatoryPersonPin?: string,
    public signatoryPersonName?: number,
    public signatoryPersonDepartment?: number,
    public signatoryPersonDesignation?: number,
    public signatoryPersonUnit?: number,
    public signatoryPersonEmail?: string,
    public signatoryPersonCell?: string,

    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public generatedByLogin?: string,
    public generatedById?: number
  ) {}
}
