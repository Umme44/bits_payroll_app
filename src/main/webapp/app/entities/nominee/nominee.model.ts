import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Status } from 'app/entities/enumerations/status.model';
import { NomineeType } from 'app/entities/enumerations/nominee-type.model';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';

export interface INominee {
  id: number;
  nomineeName?: string | null;
  presentAddress?: string | null;
  relationshipWithEmployee?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  age?: number | null;
  sharePercentage?: number | null;
  imagePath?: string | null;
  status?: Status | null;
  guardianName?: string | null;
  guardianFatherName?: string | null;
  guardianSpouseName?: string | null;
  guardianDateOfBirth?: dayjs.Dayjs | null;
  guardianPresentAddress?: string | null;
  guardianDocumentName?: string | null;
  guardianRelationshipWith?: string | null;
  guardianImagePath?: string | null;
  isLocked?: boolean | null;
  nominationDate?: dayjs.Dayjs | null;
  permanentAddress?: string | null;
  guardianPermanentAddress?: string | null;
  nomineeType?: NomineeType | null;
  identityType?: IdentityType | null;
  documentName?: string | null;
  idNumber?: string | null;
  isNidVerified?: boolean | null;
  guardianIdentityType?: IdentityType | null;
  guardianIdNumber?: string | null;
  isGuardianNidVerified?: boolean | null;
  employee?: Pick<IEmployee, 'id'> | null;
  approvedBy?: Pick<IEmployee, 'id'> | null;
  witness?: Pick<IEmployee, 'id'> | null;
  member?: Pick<IEmployee, 'id'> | null;
}

export type NewNominee = Omit<INominee, 'id'> & { id: null };
