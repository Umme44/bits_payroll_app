import dayjs from 'dayjs/esm';
import { IdentityType } from '../../model/enumerations/identity-type.model';

export interface IPfNominee {
  id?: number;
  nominationDate?: dayjs.Dayjs;
  fullName?: string;
  presentAddress?: string;
  permanentAddress?: string;
  relationship?: string;
  dateOfBirth?: dayjs.Dayjs;
  age?: number;
  sharePercentage?: number;
  identityType?: IdentityType;
  documentName?: string;
  idNumber?: string;
  isNidVerified?: boolean;
  photo?: string;
  guardianName?: string;
  guardianFatherOrSpouseName?: string;
  guardianDateOfBirth?: dayjs.Dayjs;
  guardianPresentAddress?: string;
  guardianPermanentAddress?: string;
  guardianProofOfIdentityOfLegalGuardian?: string;
  guardianRelationshipWithNominee?: string;

  guardianIdentityType?: string;
  guardianIdNumber?: string;
  isGuardianNidVerified?: boolean;
  guardianDocumentName?: string;
  pfAccountId?: number;
  pin?: string;
  pfWitnessId?: number;
  approvedById?: number;
  approvedByFullName?: string;
  accHolderName?: String;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  pfWitnessFullName?: String;
  isApproved?: boolean;
  isChecked?: boolean;
}

export class PfNominee implements IPfNominee {
  constructor(
    public id?: number,
    public nominationDate?: dayjs.Dayjs,
    public fullName?: string,
    public presentAddress?: string,
    public permanentAddress?: string,
    public relationship?: string,
    public dateOfBirth?: dayjs.Dayjs,
    public age?: number,
    public sharePercentage?: number,

    public identityType?: IdentityType,
    public documentName?: string,
    public idNumber?: string,

    public isNidVerified?: boolean,
    public photo?: string,
    public guardianName?: string,
    public guardianFatherOrSpouseName?: string,
    public guardianDateOfBirth?: dayjs.Dayjs,
    public guardianPresentAddress?: string,
    public guardianPermanentAddress?: string,
    public guardianProofOfIdentityOfLegalGuardian?: string,
    public guardianRelationshipWithNominee?: string,

    public guardianIdentityType?: string,
    public guardianIdNumber?: string,
    public isGuardianNidVerified?: boolean,
    public guardianDocumentName?: string,

    public pfAccountId?: number,
    public pin?: string,
    public pfWitnessId?: number,
    public approvedById?: number,
    public accHolderName?: String,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public isApproved?: boolean,
    public isChecked?: boolean
  ) {
    this.isNidVerified = this.isNidVerified || false;
    this.isGuardianNidVerified = this.isGuardianNidVerified || false;
    this.isApproved = this.isApproved || false;
  }
}
