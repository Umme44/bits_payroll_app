import dayjs from 'dayjs/esm';
import { BloodGroup } from 'app/entities/enumerations/blood-group.model';
import { Religion } from 'app/entities/enumerations/religion.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { PayType } from 'app/entities/enumerations/pay-type.model';
import { DisbursementMethod } from 'app/entities/enumerations/disbursement-method.model';
import { CardType } from 'app/entities/enumerations/card-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { EmploymentStatus } from 'app/entities/enumerations/employment-status.model';
import { IPfNominee } from '../../shared/legacy/legacy-model/pf-nominee.model';
import { INominee } from '../../shared/legacy/legacy-model/nominee.model';
import { EmployeeCategory } from '../../shared/model/enumerations/employee-category.model';

export interface IEmployee {
  id?: number;
  referenceId?: string | null;
  pin?: string | null;
  picture?: string | null;
  fullName?: string | null;
  employeeName?: string | null;
  surName?: string | null;
  nationalIdNo?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  placeOfBirth?: string | null;
  fatherName?: string | null;
  motherName?: string | null;
  bloodGroup?: BloodGroup | null;
  presentAddress?: string | null;
  permanentAddress?: string | null;
  personalContactNo?: string | null;
  personalEmail?: string | null;
  religion?: Religion | null;
  maritalStatus?: MaritalStatus | null;
  dateOfMarriage?: dayjs.Dayjs | null;
  spouseName?: string | null;
  officialEmail?: string | null;
  officialContactNo?: string | null;
  officePhoneExtension?: string | null;
  whatsappId?: string | null;
  skypeId?: string | null;
  emergencyContactPersonName?: string | null;
  emergencyContactPersonRelationshipWithEmployee?: string | null;
  emergencyContactPersonContactNumber?: string | null;
  mainGrossSalary?: number | null;
  employeeCategory?: EmployeeCategory | null;
  location?: string | null;
  dateOfJoining?: dayjs.Dayjs | null;
  dateOfConfirmation?: dayjs.Dayjs | null;
  isProbationaryPeriodExtended?: boolean | null;
  probationPeriodExtendedTo?: dayjs.Dayjs | null;
  payType?: PayType | null;
  disbursementMethod?: DisbursementMethod | null;
  bankName?: string | null;
  bankAccountNo?: string | null;
  mobileCelling?: number | null;
  bkashNumber?: string | null;
  cardType?: CardType | null;
  cardNumber?: string | null;
  tinNumber?: string | null;
  passportNo?: string | null;
  passportPlaceOfIssue?: string | null;
  passportIssuedDate?: dayjs.Dayjs | null;
  passportExpiryDate?: dayjs.Dayjs | null;
  gender?: Gender | null;
  welfareFundDeduction?: number | null;
  employmentStatus?: EmploymentStatus | null;
  hasDisabledChild?: boolean | null;
  isFirstTimeAitGiver?: boolean | null;
  isSalaryHold?: boolean | null;
  isFestivalBonusHold?: boolean | null;
  isPhysicallyDisabled?: boolean | null;
  isFreedomFighter?: boolean | null;
  hasOverTime?: boolean | null;
  probationPeriodEndDate?: dayjs.Dayjs | null;
  contractPeriodExtendedTo?: dayjs.Dayjs | null;
  contractPeriodEndDate?: dayjs.Dayjs | null;
  cardType02?: CardType | null;
  cardNumber02?: string | null;
  cardType03?: CardType | null;
  cardNumber03?: string | null;
  allowance01?: number | null;
  allowance01EffectiveFrom?: dayjs.Dayjs | null;
  allowance01EffectiveTo?: dayjs.Dayjs | null;
  allowance02?: number | null;
  allowance02EffectiveFrom?: dayjs.Dayjs | null;
  allowance02EffectiveTo?: dayjs.Dayjs | null;
  allowance03?: number | null;
  allowance03EffectiveFrom?: dayjs.Dayjs | null;
  allowance03EffectiveTo?: dayjs.Dayjs | null;
  allowance04?: number | null;
  allowance04EffectiveFrom?: dayjs.Dayjs | null;
  allowance04EffectiveTo?: dayjs.Dayjs | null;
  allowance05?: number | null;
  allowance05EffectiveFrom?: dayjs.Dayjs | null;
  allowance05EffectiveTo?: dayjs.Dayjs | null;
  allowance06?: number | null;
  allowance06EffectiveFrom?: dayjs.Dayjs | null;
  allowance06EffectiveTo?: dayjs.Dayjs | null;
  isTaxPaidByOrganisation?: boolean | null;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  isAllowedToGiveOnlineAttendance?: boolean | null;
  noticePeriodInDays?: number | null;
  isFixedTermContract?: boolean | null;
  currentInTime?: dayjs.Dayjs | null;
  currentOutTime?: dayjs.Dayjs | null;
  onlineAttendanceSanctionedAt?: dayjs.Dayjs | null;
  isNidVerified?: boolean | null;
  canRaiseRrfOnBehalf?: boolean | null;
  taxesCircle?: string | null;
  taxesZone?: string | null;
  canManageTaxAcknowledgementReceipt?: boolean | null;
  isEligibleForAutomatedAttendance?: boolean | null;

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
  isContructualPeriodExtended?: boolean;
  isFestivalBonusDisabled?: boolean;
  currentlyResigned?: boolean;
  recruitmentRequisitionFormId?: number;
  rrfNumber?: string;
  isBillableResource?: boolean | null;
  isAugmentedResource?: boolean | null;
  lastWorkingDay?: dayjs.Dayjs | null;

  // designation?: Pick<IDesignation, 'id'> | null;
  // department?: Pick<IDepartment, 'id'> | null;
  // reportingTo?: Pick<IEmployee, 'id'> | null;
  // nationality?: Pick<INationality, 'id' | 'nationalityName'> | null;
  // bankBranch?: Pick<IBankBranch, 'id'> | null;
  // band?: Pick<IBand, 'id'> | null;
  // unit?: Pick<IUnit, 'id'> | null;
  // user?: Pick<IUser, 'id' | 'login'> | null;
  //isChecked: boolean;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
