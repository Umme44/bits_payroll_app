import dayjs from 'dayjs/esm';
import { PurposeOfNOC } from 'app/shared/model/enumerations/purpose-of-noc.model';
import { CertificateStatus } from 'app/shared/model/enumerations/certificate-status.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IEmployeeNOC {
  id?: number;
  passportNumber?: string;
  leaveStartDate?: dayjs.Dayjs;
  leaveEndDate?: dayjs.Dayjs;
  countryToVisit?: string;
  referenceNumber?: string;
  issueDate?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  generatedAt?: dayjs.Dayjs;
  reason?: string;
  purposeOfNOC?: PurposeOfNOC;
  certificateStatus?: CertificateStatus;
  isRequiredForVisa?: boolean;
  employeeId?: number;
  employeeName?: string;
  employeeSurName?: string;
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

export class EmployeeNOC implements IEmployeeNOC {
  constructor(
    public id?: number,
    public passportNumber?: string,
    public leaveStartDate?: dayjs.Dayjs,
    public leaveEndDate?: dayjs.Dayjs,
    public countryToVisit?: string,
    public referenceNumber?: string,
    public issueDate?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public generatedAt?: dayjs.Dayjs,
    public reason?: string,
    public purposeOfNOC?: PurposeOfNOC,
    public certificateStatus?: CertificateStatus,
    public isRequiredForVisa?: boolean,
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
  ) {
    this.isRequiredForVisa = this.isRequiredForVisa || false;
  }
}
