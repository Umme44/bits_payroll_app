import dayjs from 'dayjs/esm';
import { PfAccountStatus } from 'app/entities/enumerations/pf-account-status.model';

export interface IPfAccount {
  id: number;
  pfCode?: string | null;
  membershipStartDate?: dayjs.Dayjs | null;
  membershipEndDate?: dayjs.Dayjs | null;
  status?: PfAccountStatus | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
  accHolderName?: string | null;
  pin?: string | null;
  dateOfJoining?: dayjs.Dayjs | null;
  dateOfConfirmation?: dayjs.Dayjs | null;
}

export type NewPfAccount = Omit<IPfAccount, 'id'> & { id: null };
