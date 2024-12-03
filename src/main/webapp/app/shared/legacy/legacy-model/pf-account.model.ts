import dayjs from 'dayjs/esm';
import { PfAccountStatus } from 'app/shared/model/enumerations/pf-account-status.model';

export interface IPfAccount {
  id?: number;
  pfCode?: string;
  membershipStartDate?: dayjs.Dayjs;
  membershipEndDate?: dayjs.Dayjs;
  status?: PfAccountStatus;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  accHolderName?: string;
  pin?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
}

export class PfAccount implements IPfAccount {
  constructor(
    public id?: number,
    public pfCode?: string,
    public membershipStartDate?: dayjs.Dayjs,
    public membershipEndDate?: dayjs.Dayjs,
    public status?: PfAccountStatus,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public accHolderName?: string,
    public pin?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs
  ) {}
}
