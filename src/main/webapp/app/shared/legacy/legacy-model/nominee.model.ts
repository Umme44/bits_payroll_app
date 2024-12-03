import dayjs from 'dayjs/esm';
import { NomineeType } from 'app/shared/model/enumerations/nominee-type.model';
import { IdentityType } from 'app/shared/model/enumerations/identity-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { EmployeeCategory } from '../../model/enumerations/employee-category.model';

export interface INominee {
  id?: number;
  nomineeName?: string;
  presentAddress?: string;
  relationshipWithEmployee?: string;
  dateOfBirth?: dayjs.Dayjs;
  age?: number;
  sharePercentage?: number;
  imagePath?: string;
  guardianName?: string;
  guardianFatherName?: string;
  guardianSpouseName?: string;
  guardianDateOfBirth?: dayjs.Dayjs;
  guardianPresentAddress?: string;
  guardianIdentityType?: IdentityType;
  guardianIdNumber?: string;
  guardianDocumentName?: string;
  guardianRelationshipWith?: string;
  guardianImagePath?: string;
  isLocked?: boolean;
  nominationDate?: dayjs.Dayjs;
  permanentAddress?: string;
  guardianPermanentAddress?: string;
  nomineeType?: NomineeType;
  identityType?: IdentityType;
  documentName?: string;
  idNumber?: string;
  isNidVerified?: boolean;
  isGuardianNidVerified?: boolean;
  employeeId?: number;
  approvedById?: number;
  witnessId?: number;
  memberId?: number;
  pin?: string;
  fullName?: string;
  status?: Status;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  employeeCategory?: EmployeeCategory;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  isChecked?: boolean;
  approvedByFullName?: string;
}

export class Nominee implements INominee {
  constructor(
    public id?: number,
    public nomineeName?: string,
    public presentAddress?: string,
    public relationshipWithEmployee?: string,
    public dateOfBirth?: dayjs.Dayjs,
    public age?: number,
    public sharePercentage?: number,
    public imagePath?: string,
    public guardianName?: string,
    public guardianFatherName?: string,
    public guardianSpouseName?: string,
    public guardianDateOfBirth?: dayjs.Dayjs,
    public guardianPresentAddress?: string,
    public guardianDocumentName?: string,
    public guardianRelationshipWith?: string,
    public guardianImagePath?: string,
    public isLocked?: boolean,
    public nominationDate?: dayjs.Dayjs,
    public permanentAddress?: string,
    public guardianPermanentAddress?: string,
    public nomineeType?: NomineeType,
    public identityType?: IdentityType,
    public documentName?: string,
    public idNumber?: string,
    public isNidVerified?: boolean,
    public guardianIdentityType?: IdentityType,
    public guardianIdNumber?: string,
    public isGuardianNidVerified?: boolean,
    public employeeId?: number,
    public approvedById?: number,
    public witnessId?: number,
    public memberId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public employeeCategory?: EmployeeCategory,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public isChecked?: boolean,
    public approvedByFullName?: string
  ) {
    this.isLocked = this.isLocked || false;
    this.isNidVerified = this.isNidVerified || false;
    this.isGuardianNidVerified = this.isGuardianNidVerified || false;
  }
}
