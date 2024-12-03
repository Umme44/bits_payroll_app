import { IOrganization, NewOrganization } from './organization.model';

export const sampleWithRequiredData: IOrganization = {
  id: 12784,
  shortName: 'Automated Rial',
  fullName: 'Hawaii TCP',
  domainName: 'Arizona quantify systems',
  emailAddress: 'withdrawal invoice invoice',
};

export const sampleWithPartialData: IOrganization = {
  id: 59530,
  shortName: 'Games Metal payment',
  fullName: 'Practical Frozen Phased',
  slogan: 'Camp Bike U.S.',
  domainName: 'Future Implementation turn-key',
  emailAddress: 'SCSI national bus',
  hrEmailAddress: 'Azerbaijan Practical plug-and-play',
  noReplyEmailAddress: 'bottom-line invoice programming',
  contactNumber: 'Grocery generating interactive',
  financeManagerSignature: '../fake-data/blob/hipster.txt',
  logo: '../fake-data/blob/hipster.txt',
  pfStatementLetterHead: '../fake-data/blob/hipster.txt',
  salaryPayslipLetterHead: '../fake-data/blob/hipster.txt',
  festivalBonusPayslipLetterHead: '../fake-data/blob/hipster.txt',
  recruitmentRequisitionLetterHead: '../fake-data/blob/hipster.txt',
  hasOrganizationStamp: false,
  twitter: 'parsing Salad',
};

export const sampleWithFullData: IOrganization = {
  id: 60960,
  shortName: 'Tools Sleek',
  fullName: 'azure e-services',
  slogan: 'Future website Granite',
  domainName: 'Cheese',
  emailAddress: 'collaborative Applications executive',
  hrEmailAddress: 'disintermediate',
  noReplyEmailAddress: 'Horizontal',
  contactNumber: 'Tactics Togo action-items',
  financeManagerPIN: 'viral orchestrate',
  financeManagerSignature: '../fake-data/blob/hipster.txt',
  logo: '../fake-data/blob/hipster.txt',
  documentLetterHead: '../fake-data/blob/hipster.txt',
  pfStatementLetterHead: '../fake-data/blob/hipster.txt',
  taxStatementLetterHead: '../fake-data/blob/hipster.txt',
  nomineeLetterHead: '../fake-data/blob/hipster.txt',
  salaryPayslipLetterHead: '../fake-data/blob/hipster.txt',
  festivalBonusPayslipLetterHead: '../fake-data/blob/hipster.txt',
  recruitmentRequisitionLetterHead: '../fake-data/blob/hipster.txt',
  hasOrganizationStamp: false,
  organizationStamp: '../fake-data/blob/hipster.txt',
  linkedin: 'Intranet Won parallelism',
  twitter: 'Applications',
  facebook: 'Pre-emptive bottom-line approach',
  youtube: 'forecast back-end',
  instagram: 'Tennessee sensor Movies',
  whatsapp: 'defect Incredible Division',
};

export const sampleWithNewData: NewOrganization = {
  shortName: 'Legacy holistic',
  fullName: 'Checking state gold',
  domainName: 'Lilangeni',
  emailAddress: 'salmon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
