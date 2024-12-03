import { OrganizationFileType } from 'app/shared/model/enumerations/organization-file-type.enum';

export interface IOrganization {
  id?: number;
  shortName?: string;
  fullName?: string;
  slogan?: string;
  domainName?: string;
  emailAddress?: string;
  hrEmailAddress?: string;
  noReplyEmailAddress?: string;
  contactNumber?: string;
  financeManagerPIN?: string;
  financeManagerName?: string;
  financeManagerDesignation?: string;
  financeManagerUnit?: string;
  financeManagerSignature?: any;
  logo?: any;
  documentLetterHead?: any;
  pfStatementLetterHead?: any;
  taxStatementLetterHead?: any;
  nomineeLetterHead?: any;
  salaryPayslipLetterHead?: any;
  festivalBonusPayslipLetterHead?: any;
  recruitmentRequisitionLetterHead?: any;
  hasOrganizationStamp?: boolean;
  organizationStamp?: any;
  linkedin?: string;
  twitter?: string;
  facebook?: string;
  youtube?: string;
  instagram?: string;
  whatsapp?: string;
  organizationFileTypeList?: OrganizationFileType[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public shortName?: string,
    public fullName?: string,
    public slogan?: string,
    public domainName?: string,
    public emailAddress?: string,
    public hrEmailAddress?: string,
    public noReplyEmailAddress?: string,
    public contactNumber?: string,
    public financeManagerPIN?: string,
    public financeManagerSignature?: any,
    public logo?: any,
    public documentLetterHead?: any,
    public pfStatementLetterHead?: any,
    public taxStatementLetterHead?: any,
    public nomineeLetterHead?: any,
    public salaryPayslipLetterHead?: any,
    public festivalBonusPayslipLetterHead?: any,
    public recruitmentRequisitionLetterHead?: any,
    public hasOrganizationStamp?: boolean,
    public organizationStamp?: any,
    public linkedin?: string,
    public twitter?: string,
    public facebook?: string,
    public youtube?: string,
    public instagram?: string,
    public whatsapp?: string,
    public financeManagerName?: string,
    public financeManagerDesignation?: string,
    public financeManagerUnit?: string
  ) {
    this.hasOrganizationStamp = this.hasOrganizationStamp || false;
  }
}
