import dayjs from 'dayjs/esm';
import { BloodGroup } from 'app/shared/model/enumerations/blood-group.model';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import { IPfNominee } from '../../entities/nominee-admin/pf-nominee.model';
import { INominee } from '../../entities/nominee-admin/nominee.model';

export interface INomineeMaster {
  id?: number;
  pin?: string;
  picture?: string;
  fullName?: string;
  dateOfBirth?: dayjs.Dayjs;
  bloodGroup?: BloodGroup;
  presentAddress?: string;
  permanentAddress?: string;
  personalContactNo?: string;
  officialEmail?: string;
  officialContactNo?: string;
  officePhoneExtension?: string;
  whatsappId?: string;
  skypeId?: string;
  employeeCategory?: EmployeeCategory;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  gender?: Gender;
  employmentStatus?: EmploymentStatus;
  designationName?: string;
  departmentName?: string;
  reportingToName?: string;
  bandName?: string;
  unitName?: string;
  pfNomineeDTOList?: IPfNominee[];
  nomineeList?: INominee[];
  isChecked?: boolean;
  generalSharePercentage?: number;
  gfSharePercentage?: number;
  pfSharePercentage?: number;
  isAllGFNomineeApproved?: string;
  isAllGeneralNomineeApproved?: string;
  isAllPfNomineeApproved?: string;
}

export class NomineeMaster implements INomineeMaster {
  constructor(
    public id?: number,
    public pin?: string,
    public picture?: string,
    public fullName?: string,
    public dateOfBirth?: dayjs.Dayjs,
    public bloodGroup?: BloodGroup,
    public presentAddress?: string,
    public permanentAddress?: string,
    public personalContactNo?: string,
    public officialEmail?: string,
    public officialContactNo?: string,
    public officePhoneExtension?: string,
    public whatsappId?: string,
    public skypeId?: string,
    public employeeCategory?: EmployeeCategory,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public gender?: Gender,
    public employmentStatus?: EmploymentStatus,
    public designationName?: string,
    public departmentName?: string,
    public reportingToName?: string,
    public bandName?: string,
    public unitName?: string,
    public pfNomineeDTOList?: IPfNominee[],
    public nomineeList?: INominee[],
    public isChecked?: boolean,
    public generalSharePercentage?: number,
    public gfSharePercentage?: number,
    public pfSharePercentage?: number,
    public isAllGFNomineeApproved?: string,
    public isAllPfNomineeApproved?: string,
    public isAllGeneralNomineeApproved?: string
  ) {
    this.pfNomineeDTOList = [];
    this.nomineeList = [];
  }
}
