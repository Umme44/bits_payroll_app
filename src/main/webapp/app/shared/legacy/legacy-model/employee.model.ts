import { BloodGroup } from 'app/shared/model/enumerations/blood-group.model';
import { Religion } from 'app/shared/model/enumerations/religion.model';
import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { PayType } from 'app/shared/model/enumerations/pay-type.model';
import { DisbursementMethod } from 'app/shared/model/enumerations/disbursement-method.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import dayjs from 'dayjs/esm';
import { IPfNominee } from './pf-nominee.model';
import { INominee } from './nominee.model';

export interface IEmployee {
  id?: number;
  referenceId?: string;
  pin?: string;
  picture?: string;
  fullName?: string;
  surName?: string;
  nationalIdNo?: string;
  dateOfBirth?: dayjs.Dayjs;
  placeOfBirth?: string;
  fatherName?: string;
  motherName?: string;
  bloodGroup?: BloodGroup;
  presentAddress?: string;
  permanentAddress?: string;
  personalContactNo?: string;
  personalEmail?: string;
  religion?: Religion;
  maritalStatus?: MaritalStatus;
  dateOfMarriage?: dayjs.Dayjs;
  spouseName?: string;
  officialEmail?: string;
  officialContactNo?: string;
  officePhoneExtension?: string;
  whatsappId?: string;
  skypeId?: string;
  emergencyContactPersonName?: string;
  emergencyContactPersonRelationshipWithEmployee?: string;
  emergencyContactPersonContactNumber?: string;
  mainGrossSalary?: number;
  employeeCategory?: EmployeeCategory;
  location?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  isProbationaryPeriodExtended?: boolean;
  probationPeriodExtendedTo?: dayjs.Dayjs;
  payType?: PayType;
  disbursementMethod?: DisbursementMethod;
  bankName?: string;
  bankAccountNo?: string;
  mobileCelling?: number;
  bkashNumber?: string;
  cardType?: CardType;
  cardNumber?: string;
  tinNumber?: string;
  taxesCircle?: string;
  taxesZone?: string;
  passportNo?: string;
  passportPlaceOfIssue?: string;
  passportIssuedDate?: dayjs.Dayjs;
  passportExpiryDate?: dayjs.Dayjs;
  gender?: Gender;
  welfareFundDeduction?: number;
  employmentStatus?: EmploymentStatus;
  hasDisabledChild?: boolean;
  isFirstTimeAitGiver?: boolean;
  isSalaryHold?: boolean;
  isFestivalBonusHold?: boolean;
  isPhysicallyDisabled?: boolean;
  isFreedomFighter?: boolean;
  hasOverTime?: boolean;
  probationPeriodEndDate?: dayjs.Dayjs;
  contractPeriodExtendedTo?: dayjs.Dayjs;
  contractPeriodEndDate?: dayjs.Dayjs;
  cardType02?: CardType;
  cardNumber02?: string;
  cardType03?: CardType;
  cardNumber03?: string;
  allowance01?: number;
  allowance01EffectiveFrom?: dayjs.Dayjs;
  allowance01EffectiveTo?: dayjs.Dayjs;
  allowance02?: number;
  allowance02EffectiveFrom?: dayjs.Dayjs;
  allowance02EffectiveTo?: dayjs.Dayjs;
  allowance03?: number;
  allowance03EffectiveFrom?: dayjs.Dayjs;
  allowance03EffectiveTo?: dayjs.Dayjs;
  allowance04?: number;
  allowance04EffectiveFrom?: dayjs.Dayjs;
  allowance04EffectiveTo?: dayjs.Dayjs;
  allowance05?: number;
  allowance05EffectiveFrom?: dayjs.Dayjs;
  allowance05EffectiveTo?: dayjs.Dayjs;
  allowance06?: number;
  allowance06EffectiveFrom?: dayjs.Dayjs;
  allowance06EffectiveTo?: dayjs.Dayjs;
  isTaxPaidByOrganisation?: boolean;
  createdBy?: string;
  createdAt?: dayjs.Dayjs;
  updatedBy?: string;
  updatedAt?: dayjs.Dayjs;
  isAllowedToGiveOnlineAttendance?: boolean;
  noticePeriodInDays?: number;
  isFixedTermContract?: boolean;
  currentInTime?: dayjs.Dayjs;
  currentOutTime?: dayjs.Dayjs;
  onlineAttendanceSanctionedAt?: dayjs.Dayjs;
  isNidVerified?: boolean;
  canManageTaxAcknowledgementReceipt?: boolean;
  canRaiseRrfOnBehalf?: boolean;
  isEligibleForAutomatedAttendance?: boolean;
  designationId?: number;
  departmentId?: number;
  reportingToId?: number;
  nationalityNationalityName?: string;
  nationalityId?: number;
  bankBranchId?: number;
  bandId?: number;
  unitId?: number;
  designationName?: string;
  departmentName?: string;
  reportingToName?: string;
  reportingToPin?: string;
  bankBranchName?: string;
  bandName?: string;
  unitName?: string;
  userLogin?: string;
  userId?: number;
  pfNomineeDTOList?: IPfNominee[];
  nomineeList?: INominee[];
  isAllGFNomineeApproved?: string;
  isAllGeneralNomineeApproved?: string;
  isAllPfNomineeApproved?: string;
  isChecked?: boolean;
  isFestivalBonusDisabled?: boolean;
  recruitmentRequisitionFormId?: number;
  rrfNumber?: string;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public referenceId?: string,
    public pin?: string,
    public picture?: string,
    public fullName?: string,
    public surName?: string,
    public nationalIdNo?: string,
    public dateOfBirth?: dayjs.Dayjs,
    public placeOfBirth?: string,
    public fatherName?: string,
    public motherName?: string,
    public bloodGroup?: BloodGroup,
    public presentAddress?: string,
    public permanentAddress?: string,
    public personalContactNo?: string,
    public personalEmail?: string,
    public religion?: Religion,
    public maritalStatus?: MaritalStatus,
    public dateOfMarriage?: dayjs.Dayjs,
    public spouseName?: string,
    public officialEmail?: string,
    public officialContactNo?: string,
    public officePhoneExtension?: string,
    public whatsappId?: string,
    public skypeId?: string,
    public emergencyContactPersonName?: string,
    public emergencyContactPersonRelationshipWithEmployee?: string,
    public emergencyContactPersonContactNumber?: string,
    public mainGrossSalary?: number,
    public employeeCategory?: EmployeeCategory,
    public location?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public isProbationaryPeriodExtended?: boolean,
    public probationPeriodExtendedTo?: dayjs.Dayjs,
    public payType?: PayType,
    public disbursementMethod?: DisbursementMethod,
    public bankName?: string,
    public bankAccountNo?: string,
    public mobileCelling?: number,
    public bkashNumber?: string,
    public cardType?: CardType,
    public cardNumber?: string,
    public tinNumber?: string,
    public taxesCircle?: string,
    public taxesZone?: string,
    public passportNo?: string,
    public passportPlaceOfIssue?: string,
    public passportIssuedDate?: dayjs.Dayjs,
    public passportExpiryDate?: dayjs.Dayjs,
    public gender?: Gender,
    public welfareFundDeduction?: number,
    public employmentStatus?: EmploymentStatus,
    public hasDisabledChild?: boolean,
    public isFirstTimeAitGiver?: boolean,
    public isSalaryHold?: boolean,
    public isFestivalBonusHold?: boolean,
    public isPhysicallyDisabled?: boolean,
    public isFreedomFighter?: boolean,
    public hasOverTime?: boolean,
    public probationPeriodEndDate?: dayjs.Dayjs,
    public contractPeriodExtendedTo?: dayjs.Dayjs,
    public contractPeriodEndDate?: dayjs.Dayjs,
    public cardType02?: CardType,
    public cardNumber02?: string,
    public cardType03?: CardType,
    public cardNumber03?: string,
    public allowance01?: number,
    public allowance01EffectiveFrom?: dayjs.Dayjs,
    public allowance01EffectiveTo?: dayjs.Dayjs,
    public allowance02?: number,
    public allowance02EffectiveFrom?: dayjs.Dayjs,
    public allowance02EffectiveTo?: dayjs.Dayjs,
    public allowance03?: number,
    public allowance03EffectiveFrom?: dayjs.Dayjs,
    public allowance03EffectiveTo?: dayjs.Dayjs,
    public allowance04?: number,
    public allowance04EffectiveFrom?: dayjs.Dayjs,
    public allowance04EffectiveTo?: dayjs.Dayjs,
    public allowance05?: number,
    public allowance05EffectiveFrom?: dayjs.Dayjs,
    public allowance05EffectiveTo?: dayjs.Dayjs,
    public allowance06?: number,
    public allowance06EffectiveFrom?: dayjs.Dayjs,
    public allowance06EffectiveTo?: dayjs.Dayjs,
    public isTaxPaidByOrganisation?: boolean,
    public createdBy?: string,
    public createdAt?: dayjs.Dayjs,
    public updatedBy?: string,
    public updatedAt?: dayjs.Dayjs,
    public isAllowedToGiveOnlineAttendance?: boolean,
    public noticePeriodInDays?: number,
    public isFixedTermContract?: boolean,
    public currentInTime?: dayjs.Dayjs,
    public currentOutTime?: dayjs.Dayjs,
    public onlineAttendanceSanctionedAt?: dayjs.Dayjs,
    public isNidVerified?: boolean,
    public canManageTaxAcknowledgementReceipt?: boolean,
    public canRaiseRrfOnBehalf?: boolean,
    public isEligibleForAutomatedAttendance?: boolean,
    public designationId?: number,
    public departmentId?: number,
    public reportingToId?: number,
    public nationalityNationalityName?: string,
    public nationalityId?: number,
    public bankBranchId?: number,
    public bandId?: number,
    public unitId?: number,
    public designationName?: string,
    public departmentName?: string,
    public reportingToName?: string,
    public bankBranchName?: string,
    public bandName?: string,
    public unitName?: string,
    public userLogin?: string,
    public userId?: number,
    public isChecked?: boolean,
    public isFestivalBonusDisabled?: boolean,
    public recruitmentRequisitionFormId?: number,
    public rrfNumber?: string
  ) {
    this.isProbationaryPeriodExtended = this.isProbationaryPeriodExtended || false;
    this.hasDisabledChild = this.hasDisabledChild || false;
    this.isFirstTimeAitGiver = this.isFirstTimeAitGiver || false;
    this.isSalaryHold = this.isSalaryHold || false;
    this.isFestivalBonusHold = this.isFestivalBonusHold || false;
    this.isPhysicallyDisabled = this.isPhysicallyDisabled || false;
    this.isFreedomFighter = this.isFreedomFighter || false;
    this.hasOverTime = this.hasOverTime || false;
    this.isTaxPaidByOrganisation = this.isTaxPaidByOrganisation || false;
    this.isAllowedToGiveOnlineAttendance = this.isAllowedToGiveOnlineAttendance || false;
    this.isFixedTermContract = this.isFixedTermContract || false;
    this.isNidVerified = this.isNidVerified || false;
    this.canRaiseRrfOnBehalf = this.canRaiseRrfOnBehalf || false;
    this.canManageTaxAcknowledgementReceipt = this.canManageTaxAcknowledgementReceipt || false;
    this.isEligibleForAutomatedAttendance = this.isEligibleForAutomatedAttendance || false;
    this.isFestivalBonusDisabled = this.isFestivalBonusDisabled || false;
  }
}
