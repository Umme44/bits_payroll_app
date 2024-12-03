import { OrganizationFileType } from '../../shared/model/enumerations/organization-file-type.enum';

export interface IOrganization {
  id: number;
  shortName?: string | null;
  fullName?: string | null;
  slogan?: string | null;
  domainName?: string | null;
  emailAddress?: string | null;
  hrEmailAddress?: string | null;
  noReplyEmailAddress?: string | null;
  contactNumber?: string | null;
  financeManagerPIN?: string | null;
  financeManagerName?: string;
  financeManagerDesignation?: string;
  financeManagerUnit?: string;
  financeManagerSignature?: string | null;
  logo?: string | null;
  documentLetterHead?: string | null;
  pfStatementLetterHead?: string | null;
  taxStatementLetterHead?: string | null;
  nomineeLetterHead?: string | null;
  salaryPayslipLetterHead?: string | null;
  festivalBonusPayslipLetterHead?: string | null;
  recruitmentRequisitionLetterHead?: string | null;
  hasOrganizationStamp?: boolean | null;
  organizationStamp?: string | null;
  linkedin?: string | null;
  twitter?: string | null;
  facebook?: string | null;
  youtube?: string | null;
  instagram?: string | null;
  whatsapp?: string | null;
  organizationFileTypeList?: OrganizationFileType[];
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
