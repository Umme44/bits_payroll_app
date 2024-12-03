import dayjs from 'dayjs/esm';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';

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
  pfAccount?: Pick<IPfAccount, 'id'> | null;
  pfWitness?: Pick<IEmployee, 'id'> | null;
  approvedBy?: Pick<IEmployee, 'id'> | null;
}

export type NewPfNominee = Omit<IPfNominee, 'id'> & { id: null };
