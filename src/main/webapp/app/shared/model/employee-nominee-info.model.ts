import dayjs from 'dayjs/esm';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import { NomineeStatus } from './enumerations/nominee-status.model';
import { IPfNominee } from '../legacy/legacy-model/pf-nominee.model';
import { INominee } from '../legacy/legacy-model/nominee.model';

export interface IEmployeeNomineeInfo {
  id?: number;
  referenceId?: string;
  pin?: string;
  picture?: string;
  fullName?: string;
  surName?: string;
  nationalIdNo?: string;
  dateOfBirth?: dayjs.Dayjs;
  placeOfBirth?: string;
  officialEmail?: string;
  officialContactNo?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  designationId?: number;
  departmentId?: number;
  reportingToId?: number;
  nationalityNationalityName?: string;
  nationalityId?: number;
  bankBranchId?: number;
  bandId?: number;
  unitId?: number;
  designationName?: string;
  departmentName?: string;
  reportingToName?: string;
  bankBranchName?: string;
  bandName?: string;
  unitName?: string;
  userLogin?: string;
  userId?: number;
  employeeCategory?: EmployeeCategory;
  employmentStatus?: EmploymentStatus;
  pfNomineeDTOList?: IPfNominee[];
  nomineeList?: INominee[];
  isAllGFNomineeApproved?: NomineeStatus;
  isAllGeneralNomineeApproved?: NomineeStatus;
  isAllPfNomineeApproved?: NomineeStatus;
  isChecked?: boolean;
}

export class EmployeeNomineeInfo implements IEmployeeNomineeInfo {
  constructor(
    public id?: number,
    public referenceId?: string,
    public pin?: string,
    public picture?: string,
    public fullName?: string,
    public surName?: string,
    public officialEmail?: string,
    public officialContactNo?: string,
    public officePhoneExtension?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public designationId?: number,
    public departmentId?: number,
    public reportingToId?: number,
    public employmentStatus?: EmploymentStatus,
    public employeeCategory?: EmployeeCategory,
    public nationalityNationalityName?: string,
    public nationalityId?: number,
    public bankBranchId?: number,
    public bandId?: number,
    public unitId?: number,
    public designationName?: string,
    public departmentName?: string,
    public reportingToName?: string,
    public pfNomineeDTOList?: IPfNominee[],
    public nomineeList?: INominee[],
    public isAllGFNomineeApproved?: NomineeStatus,
    public isAllPfNomineeApproved?: NomineeStatus,
    public isAllGeneralNomineeApproved?: NomineeStatus,
    public isChecked?: boolean
  ) {
    this.pfNomineeDTOList = [];
    this.nomineeList = [];
  }
}
