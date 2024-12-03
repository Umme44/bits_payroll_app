import dayjs from 'dayjs/esm';
import { IdentityType } from '../../shared/model/enumerations/identity-type.model';

export interface IPfNominee {
  id: number;
  nominationDate?: dayjs.Dayjs | null;
  fullName?: string | null;
  presentAddress?: string | null;
  permanentAddress?: string | null;
  relationship?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  age?: number | null;
  sharePercentage?: number | null;
  nidNumber?: string | null;
  isNidVerified?: boolean | null;
  passportNumber?: string | null;
  brnNumber?: string | null;
  photo?: string | null;
  guardianName?: string | null;
  guardianFatherOrSpouseName?: string | null;
  guardianDateOfBirth?: dayjs.Dayjs | null;
  guardianPresentAddress?: string | null;
  guardianPermanentAddress?: string | null;
  guardianProofOfIdentityOfLegalGuardian?: string | null;
  guardianRelationshipWithNominee?: string | null;
  guardianNidNumber?: string | null;
  guardianBrnNumber?: string | null;
  guardianIdNumber?: string | null;
  isGuardianNidVerified?: boolean | null;
  isApproved?: boolean | null;
  identityType?: IdentityType | null;
  idNumber?: string | null;
  documentName?: string | null;
  guardianIdentityType?: IdentityType | null;
  guardianDocumentName?: string | null;

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
  isChecked?: boolean;

  // pfAccount?: Pick<IPfAccount, 'id'> | null;
  // pfWitness?: Pick<IEmployee, 'id'> | null;
  // approvedBy?: Pick<IEmployee, 'id'> | null;
}

export type NewPfNominee = Omit<IPfNominee, 'id'> & { id: null };

export class PfNominee implements IPfNominee {
  accHolderName: String;
  age: number | null;
  approvedByFullName: string;
  approvedById: number;
  brnNumber: string | null;
  dateOfBirth: dayjs.Dayjs | null;
  departmentName: string;
  designationName: string;
  documentName: string | null;
  fullName: string | null;
  guardianBrnNumber: string | null;
  guardianDateOfBirth: dayjs.Dayjs | null;
  guardianDocumentName: string | null;
  guardianFatherOrSpouseName: string | null;
  guardianIdNumber: string | null;
  guardianIdentityType: IdentityType | null;
  guardianName: string | null;
  guardianNidNumber: string | null;
  guardianPermanentAddress: string | null;
  guardianPresentAddress: string | null;
  guardianProofOfIdentityOfLegalGuardian: string | null;
  guardianRelationshipWithNominee: string | null;
  id: number;
  idNumber: string | null;
  identityType: IdentityType | null;
  isApproved: boolean | null;
  isChecked: boolean;
  isGuardianNidVerified: boolean | null;
  isNidVerified: boolean | null;
  nidNumber: string | null;
  nominationDate: dayjs.Dayjs | null;
  passportNumber: string | null;
  permanentAddress: string | null;
  pfAccountId: number;
  pfWitnessFullName: String;
  pfWitnessId: number;
  photo: string | null;
  pin: string;
  presentAddress: string | null;
  relationship: string | null;
  sharePercentage: number | null;
  unitName: string;
}
