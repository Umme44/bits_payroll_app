import dayjs from 'dayjs/esm';
import {IPfAccount} from 'app/entities/pf-account/pf-account.model';
import {PfCollectionType} from 'app/entities/enumerations/pf-collection-type.model';

export interface IPfCollection {
  id: number;
  employeeContribution?: number | null;
  employerContribution?: number | null;
  transactionDate?: dayjs.Dayjs | null;
  year?: number | null;
  month?: number | null;
  collectionType?: PfCollectionType | null;
  employeeInterest?: number | null;
  employerInterest?: number | null;
  gross?: number | null;
  basic?: number | null;
  // pfAccount?: Pick<IPfAccount, 'id'> | null;

  pfAccountId?: number | null;
  pfCode?: string | null;
  accHolderName?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
  status?: string | null;

}

export type NewPfCollection = Omit<IPfCollection, 'id'> & { id: null };
