import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployee | NewEmployee> = Omit<
  T,
  'createdAt' | 'currentInTime' | 'currentOutTime' | 'onlineAttendanceSanctionedAt'
> & {
  createdAt?: string | null;
  currentInTime?: string | null;
  currentOutTime?: string | null;
  onlineAttendanceSanctionedAt?: string | null;
};

type EmployeeFormRawValue = FormValueOf<IEmployee>;

type NewEmployeeFormRawValue = FormValueOf<NewEmployee>;

type EmployeeFormDefaults = Pick<
  NewEmployee,
  | 'id'
  | 'isProbationaryPeriodExtended'
  | 'hasDisabledChild'
  | 'isFirstTimeAitGiver'
  | 'isSalaryHold'
  | 'isFestivalBonusHold'
  | 'isPhysicallyDisabled'
  | 'isFreedomFighter'
  | 'hasOverTime'
  | 'isTaxPaidByOrganisation'
  | 'createdAt'
  | 'isAllowedToGiveOnlineAttendance'
  | 'isFixedTermContract'
  | 'currentInTime'
  | 'currentOutTime'
  | 'onlineAttendanceSanctionedAt'
  | 'isNidVerified'
  | 'canRaiseRrfOnBehalf'
  | 'canManageTaxAcknowledgementReceipt'
  | 'isEligibleForAutomatedAttendance'
  | 'isBillableResource'
  | 'isAugmentedResource'
>;

type EmployeeFormGroupContent = {
  id: FormControl<EmployeeFormRawValue['id'] | NewEmployee['id']>;
  referenceId: FormControl<EmployeeFormRawValue['referenceId']>;
  pin: FormControl<EmployeeFormRawValue['pin']>;
  picture: FormControl<EmployeeFormRawValue['picture']>;
  fullName: FormControl<EmployeeFormRawValue['fullName']>;
  surName: FormControl<EmployeeFormRawValue['surName']>;
  nationalIdNo: FormControl<EmployeeFormRawValue['nationalIdNo']>;
  dateOfBirth: FormControl<EmployeeFormRawValue['dateOfBirth']>;
  placeOfBirth: FormControl<EmployeeFormRawValue['placeOfBirth']>;
  fatherName: FormControl<EmployeeFormRawValue['fatherName']>;
  motherName: FormControl<EmployeeFormRawValue['motherName']>;
  bloodGroup: FormControl<EmployeeFormRawValue['bloodGroup']>;
  presentAddress: FormControl<EmployeeFormRawValue['presentAddress']>;
  permanentAddress: FormControl<EmployeeFormRawValue['permanentAddress']>;
  personalContactNo: FormControl<EmployeeFormRawValue['personalContactNo']>;
  personalEmail: FormControl<EmployeeFormRawValue['personalEmail']>;
  religion: FormControl<EmployeeFormRawValue['religion']>;
  maritalStatus: FormControl<EmployeeFormRawValue['maritalStatus']>;
  dateOfMarriage: FormControl<EmployeeFormRawValue['dateOfMarriage']>;
  spouseName: FormControl<EmployeeFormRawValue['spouseName']>;
  officialEmail: FormControl<EmployeeFormRawValue['officialEmail']>;
  officialContactNo: FormControl<EmployeeFormRawValue['officialContactNo']>;
  officePhoneExtension: FormControl<EmployeeFormRawValue['officePhoneExtension']>;
  whatsappId: FormControl<EmployeeFormRawValue['whatsappId']>;
  skypeId: FormControl<EmployeeFormRawValue['skypeId']>;
  emergencyContactPersonName: FormControl<EmployeeFormRawValue['emergencyContactPersonName']>;
  emergencyContactPersonRelationshipWithEmployee: FormControl<EmployeeFormRawValue['emergencyContactPersonRelationshipWithEmployee']>;
  emergencyContactPersonContactNumber: FormControl<EmployeeFormRawValue['emergencyContactPersonContactNumber']>;
  mainGrossSalary: FormControl<EmployeeFormRawValue['mainGrossSalary']>;
  employeeCategory: FormControl<EmployeeFormRawValue['employeeCategory']>;
  location: FormControl<EmployeeFormRawValue['location']>;
  dateOfJoining: FormControl<EmployeeFormRawValue['dateOfJoining']>;
  dateOfConfirmation: FormControl<EmployeeFormRawValue['dateOfConfirmation']>;
  isProbationaryPeriodExtended: FormControl<EmployeeFormRawValue['isProbationaryPeriodExtended']>;
  probationPeriodExtendedTo: FormControl<EmployeeFormRawValue['probationPeriodExtendedTo']>;
  payType: FormControl<EmployeeFormRawValue['payType']>;
  disbursementMethod: FormControl<EmployeeFormRawValue['disbursementMethod']>;
  bankName: FormControl<EmployeeFormRawValue['bankName']>;
  bankAccountNo: FormControl<EmployeeFormRawValue['bankAccountNo']>;
  mobileCelling: FormControl<EmployeeFormRawValue['mobileCelling']>;
  bkashNumber: FormControl<EmployeeFormRawValue['bkashNumber']>;
  cardType: FormControl<EmployeeFormRawValue['cardType']>;
  cardNumber: FormControl<EmployeeFormRawValue['cardNumber']>;
  tinNumber: FormControl<EmployeeFormRawValue['tinNumber']>;
  passportNo: FormControl<EmployeeFormRawValue['passportNo']>;
  passportPlaceOfIssue: FormControl<EmployeeFormRawValue['passportPlaceOfIssue']>;
  passportIssuedDate: FormControl<EmployeeFormRawValue['passportIssuedDate']>;
  passportExpiryDate: FormControl<EmployeeFormRawValue['passportExpiryDate']>;
  gender: FormControl<EmployeeFormRawValue['gender']>;
  welfareFundDeduction: FormControl<EmployeeFormRawValue['welfareFundDeduction']>;
  employmentStatus: FormControl<EmployeeFormRawValue['employmentStatus']>;
  hasDisabledChild: FormControl<EmployeeFormRawValue['hasDisabledChild']>;
  isFirstTimeAitGiver: FormControl<EmployeeFormRawValue['isFirstTimeAitGiver']>;
  isSalaryHold: FormControl<EmployeeFormRawValue['isSalaryHold']>;
  isFestivalBonusHold: FormControl<EmployeeFormRawValue['isFestivalBonusHold']>;
  isPhysicallyDisabled: FormControl<EmployeeFormRawValue['isPhysicallyDisabled']>;
  isFreedomFighter: FormControl<EmployeeFormRawValue['isFreedomFighter']>;
  hasOverTime: FormControl<EmployeeFormRawValue['hasOverTime']>;
  probationPeriodEndDate: FormControl<EmployeeFormRawValue['probationPeriodEndDate']>;
  contractPeriodExtendedTo: FormControl<EmployeeFormRawValue['contractPeriodExtendedTo']>;
  contractPeriodEndDate: FormControl<EmployeeFormRawValue['contractPeriodEndDate']>;
  cardType02: FormControl<EmployeeFormRawValue['cardType02']>;
  cardNumber02: FormControl<EmployeeFormRawValue['cardNumber02']>;
  cardType03: FormControl<EmployeeFormRawValue['cardType03']>;
  cardNumber03: FormControl<EmployeeFormRawValue['cardNumber03']>;
  allowance01: FormControl<EmployeeFormRawValue['allowance01']>;
  allowance01EffectiveFrom: FormControl<EmployeeFormRawValue['allowance01EffectiveFrom']>;
  allowance01EffectiveTo: FormControl<EmployeeFormRawValue['allowance01EffectiveTo']>;
  allowance02: FormControl<EmployeeFormRawValue['allowance02']>;
  allowance02EffectiveFrom: FormControl<EmployeeFormRawValue['allowance02EffectiveFrom']>;
  allowance02EffectiveTo: FormControl<EmployeeFormRawValue['allowance02EffectiveTo']>;
  allowance03: FormControl<EmployeeFormRawValue['allowance03']>;
  allowance03EffectiveFrom: FormControl<EmployeeFormRawValue['allowance03EffectiveFrom']>;
  allowance03EffectiveTo: FormControl<EmployeeFormRawValue['allowance03EffectiveTo']>;
  allowance04: FormControl<EmployeeFormRawValue['allowance04']>;
  allowance04EffectiveFrom: FormControl<EmployeeFormRawValue['allowance04EffectiveFrom']>;
  allowance04EffectiveTo: FormControl<EmployeeFormRawValue['allowance04EffectiveTo']>;
  allowance05: FormControl<EmployeeFormRawValue['allowance05']>;
  allowance05EffectiveFrom: FormControl<EmployeeFormRawValue['allowance05EffectiveFrom']>;
  allowance05EffectiveTo: FormControl<EmployeeFormRawValue['allowance05EffectiveTo']>;
  allowance06: FormControl<EmployeeFormRawValue['allowance06']>;
  allowance06EffectiveFrom: FormControl<EmployeeFormRawValue['allowance06EffectiveFrom']>;
  allowance06EffectiveTo: FormControl<EmployeeFormRawValue['allowance06EffectiveTo']>;
  isTaxPaidByOrganisation: FormControl<EmployeeFormRawValue['isTaxPaidByOrganisation']>;
  createdBy: FormControl<EmployeeFormRawValue['createdBy']>;
  createdAt: FormControl<EmployeeFormRawValue['createdAt']>;
  updatedBy: FormControl<EmployeeFormRawValue['updatedBy']>;
  updatedAt: FormControl<EmployeeFormRawValue['updatedAt']>;
  isAllowedToGiveOnlineAttendance: FormControl<EmployeeFormRawValue['isAllowedToGiveOnlineAttendance']>;
  noticePeriodInDays: FormControl<EmployeeFormRawValue['noticePeriodInDays']>;
  isFixedTermContract: FormControl<EmployeeFormRawValue['isFixedTermContract']>;
  currentInTime: FormControl<EmployeeFormRawValue['currentInTime']>;
  currentOutTime: FormControl<EmployeeFormRawValue['currentOutTime']>;
  onlineAttendanceSanctionedAt: FormControl<EmployeeFormRawValue['onlineAttendanceSanctionedAt']>;
  isNidVerified: FormControl<EmployeeFormRawValue['isNidVerified']>;
  canRaiseRrfOnBehalf: FormControl<EmployeeFormRawValue['canRaiseRrfOnBehalf']>;
  taxesCircle: FormControl<EmployeeFormRawValue['taxesCircle']>;
  taxesZone: FormControl<EmployeeFormRawValue['taxesZone']>;
  canManageTaxAcknowledgementReceipt: FormControl<EmployeeFormRawValue['canManageTaxAcknowledgementReceipt']>;
  isEligibleForAutomatedAttendance: FormControl<EmployeeFormRawValue['isEligibleForAutomatedAttendance']>;
  floor: FormControl<EmployeeFormRawValue['floor']>;
  deskLocation: FormControl<EmployeeFormRawValue['deskLocation']>;
  isBillableResource: FormControl<EmployeeFormRawValue['isBillableResource']>;
  isAugmentedResource: FormControl<EmployeeFormRawValue['isAugmentedResource']>;
  lastWorkingDay: FormControl<EmployeeFormRawValue['lastWorkingDay']>;
  officeLocation: FormControl<EmployeeFormRawValue['officeLocation']>;
  designation: FormControl<EmployeeFormRawValue['designation']>;
  department: FormControl<EmployeeFormRawValue['department']>;
  reportingTo: FormControl<EmployeeFormRawValue['reportingTo']>;
  nationality: FormControl<EmployeeFormRawValue['nationality']>;
  bankBranch: FormControl<EmployeeFormRawValue['bankBranch']>;
  band: FormControl<EmployeeFormRawValue['band']>;
  unit: FormControl<EmployeeFormRawValue['unit']>;
  user: FormControl<EmployeeFormRawValue['user']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({
      ...this.getFormDefaults(),
      ...employee,
    });
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referenceId: new FormControl(employeeRawValue.referenceId),
      pin: new FormControl(employeeRawValue.pin, {
        validators: [Validators.required],
      }),
      picture: new FormControl(employeeRawValue.picture),
      fullName: new FormControl(employeeRawValue.fullName),
      surName: new FormControl(employeeRawValue.surName),
      nationalIdNo: new FormControl(employeeRawValue.nationalIdNo),
      dateOfBirth: new FormControl(employeeRawValue.dateOfBirth),
      placeOfBirth: new FormControl(employeeRawValue.placeOfBirth),
      fatherName: new FormControl(employeeRawValue.fatherName),
      motherName: new FormControl(employeeRawValue.motherName),
      bloodGroup: new FormControl(employeeRawValue.bloodGroup),
      presentAddress: new FormControl(employeeRawValue.presentAddress),
      permanentAddress: new FormControl(employeeRawValue.permanentAddress),
      personalContactNo: new FormControl(employeeRawValue.personalContactNo),
      personalEmail: new FormControl(employeeRawValue.personalEmail),
      religion: new FormControl(employeeRawValue.religion),
      maritalStatus: new FormControl(employeeRawValue.maritalStatus),
      dateOfMarriage: new FormControl(employeeRawValue.dateOfMarriage),
      spouseName: new FormControl(employeeRawValue.spouseName),
      officialEmail: new FormControl(employeeRawValue.officialEmail),
      officialContactNo: new FormControl(employeeRawValue.officialContactNo),
      officePhoneExtension: new FormControl(employeeRawValue.officePhoneExtension),
      whatsappId: new FormControl(employeeRawValue.whatsappId),
      skypeId: new FormControl(employeeRawValue.skypeId),
      emergencyContactPersonName: new FormControl(employeeRawValue.emergencyContactPersonName),
      emergencyContactPersonRelationshipWithEmployee: new FormControl(employeeRawValue.emergencyContactPersonRelationshipWithEmployee),
      emergencyContactPersonContactNumber: new FormControl(employeeRawValue.emergencyContactPersonContactNumber),
      mainGrossSalary: new FormControl(employeeRawValue.mainGrossSalary),
      employeeCategory: new FormControl(employeeRawValue.employeeCategory),
      location: new FormControl(employeeRawValue.location),
      dateOfJoining: new FormControl(employeeRawValue.dateOfJoining),
      dateOfConfirmation: new FormControl(employeeRawValue.dateOfConfirmation),
      isProbationaryPeriodExtended: new FormControl(employeeRawValue.isProbationaryPeriodExtended),
      probationPeriodExtendedTo: new FormControl(employeeRawValue.probationPeriodExtendedTo),
      payType: new FormControl(employeeRawValue.payType),
      disbursementMethod: new FormControl(employeeRawValue.disbursementMethod),
      bankName: new FormControl(employeeRawValue.bankName),
      bankAccountNo: new FormControl(employeeRawValue.bankAccountNo),
      mobileCelling: new FormControl(employeeRawValue.mobileCelling),
      bkashNumber: new FormControl(employeeRawValue.bkashNumber),
      cardType: new FormControl(employeeRawValue.cardType),
      cardNumber: new FormControl(employeeRawValue.cardNumber),
      tinNumber: new FormControl(employeeRawValue.tinNumber),
      passportNo: new FormControl(employeeRawValue.passportNo),
      passportPlaceOfIssue: new FormControl(employeeRawValue.passportPlaceOfIssue),
      passportIssuedDate: new FormControl(employeeRawValue.passportIssuedDate),
      passportExpiryDate: new FormControl(employeeRawValue.passportExpiryDate),
      gender: new FormControl(employeeRawValue.gender),
      welfareFundDeduction: new FormControl(employeeRawValue.welfareFundDeduction),
      employmentStatus: new FormControl(employeeRawValue.employmentStatus),
      hasDisabledChild: new FormControl(employeeRawValue.hasDisabledChild),
      isFirstTimeAitGiver: new FormControl(employeeRawValue.isFirstTimeAitGiver),
      isSalaryHold: new FormControl(employeeRawValue.isSalaryHold),
      isFestivalBonusHold: new FormControl(employeeRawValue.isFestivalBonusHold),
      isPhysicallyDisabled: new FormControl(employeeRawValue.isPhysicallyDisabled),
      isFreedomFighter: new FormControl(employeeRawValue.isFreedomFighter),
      hasOverTime: new FormControl(employeeRawValue.hasOverTime),
      probationPeriodEndDate: new FormControl(employeeRawValue.probationPeriodEndDate),
      contractPeriodExtendedTo: new FormControl(employeeRawValue.contractPeriodExtendedTo),
      contractPeriodEndDate: new FormControl(employeeRawValue.contractPeriodEndDate),
      cardType02: new FormControl(employeeRawValue.cardType02),
      cardNumber02: new FormControl(employeeRawValue.cardNumber02),
      cardType03: new FormControl(employeeRawValue.cardType03),
      cardNumber03: new FormControl(employeeRawValue.cardNumber03),
      allowance01: new FormControl(employeeRawValue.allowance01, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance01EffectiveFrom: new FormControl(employeeRawValue.allowance01EffectiveFrom),
      allowance01EffectiveTo: new FormControl(employeeRawValue.allowance01EffectiveTo),
      allowance02: new FormControl(employeeRawValue.allowance02, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance02EffectiveFrom: new FormControl(employeeRawValue.allowance02EffectiveFrom),
      allowance02EffectiveTo: new FormControl(employeeRawValue.allowance02EffectiveTo),
      allowance03: new FormControl(employeeRawValue.allowance03, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance03EffectiveFrom: new FormControl(employeeRawValue.allowance03EffectiveFrom),
      allowance03EffectiveTo: new FormControl(employeeRawValue.allowance03EffectiveTo),
      allowance04: new FormControl(employeeRawValue.allowance04, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance04EffectiveFrom: new FormControl(employeeRawValue.allowance04EffectiveFrom),
      allowance04EffectiveTo: new FormControl(employeeRawValue.allowance04EffectiveTo),
      allowance05: new FormControl(employeeRawValue.allowance05, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance05EffectiveFrom: new FormControl(employeeRawValue.allowance05EffectiveFrom),
      allowance05EffectiveTo: new FormControl(employeeRawValue.allowance05EffectiveTo),
      allowance06: new FormControl(employeeRawValue.allowance06, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance06EffectiveFrom: new FormControl(employeeRawValue.allowance06EffectiveFrom),
      allowance06EffectiveTo: new FormControl(employeeRawValue.allowance06EffectiveTo),
      isTaxPaidByOrganisation: new FormControl(employeeRawValue.isTaxPaidByOrganisation),
      createdBy: new FormControl(employeeRawValue.createdBy),
      createdAt: new FormControl(employeeRawValue.createdAt),
      updatedBy: new FormControl(employeeRawValue.updatedBy),
      updatedAt: new FormControl(employeeRawValue.updatedAt),
      isAllowedToGiveOnlineAttendance: new FormControl(employeeRawValue.isAllowedToGiveOnlineAttendance),
      noticePeriodInDays: new FormControl(employeeRawValue.noticePeriodInDays),
      isFixedTermContract: new FormControl(employeeRawValue.isFixedTermContract),
      currentInTime: new FormControl(employeeRawValue.currentInTime),
      currentOutTime: new FormControl(employeeRawValue.currentOutTime),
      onlineAttendanceSanctionedAt: new FormControl(employeeRawValue.onlineAttendanceSanctionedAt),
      isNidVerified: new FormControl(employeeRawValue.isNidVerified),
      canRaiseRrfOnBehalf: new FormControl(employeeRawValue.canRaiseRrfOnBehalf),
      taxesCircle: new FormControl(employeeRawValue.taxesCircle, {
        validators: [Validators.maxLength(250)],
      }),
      taxesZone: new FormControl(employeeRawValue.taxesZone, {
        validators: [Validators.maxLength(250)],
      }),
      canManageTaxAcknowledgementReceipt: new FormControl(employeeRawValue.canManageTaxAcknowledgementReceipt),
      isEligibleForAutomatedAttendance: new FormControl(employeeRawValue.isEligibleForAutomatedAttendance),
      floor: new FormControl(employeeRawValue.floor),
      deskLocation: new FormControl(employeeRawValue.deskLocation),
      isBillableResource: new FormControl(employeeRawValue.isBillableResource),
      isAugmentedResource: new FormControl(employeeRawValue.isAugmentedResource),
      lastWorkingDay: new FormControl(employeeRawValue.lastWorkingDay),
      officeLocation: new FormControl(employeeRawValue.officeLocation),
      designation: new FormControl(employeeRawValue.designation),
      department: new FormControl(employeeRawValue.department),
      reportingTo: new FormControl(employeeRawValue.reportingTo),
      nationality: new FormControl(employeeRawValue.nationality),
      bankBranch: new FormControl(employeeRawValue.bankBranch),
      band: new FormControl(employeeRawValue.band),
      unit: new FormControl(employeeRawValue.unit),
      user: new FormControl(employeeRawValue.user),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return this.convertEmployeeRawValueToEmployee(form.getRawValue() as EmployeeFormRawValue | NewEmployeeFormRawValue);
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({ ...this.getFormDefaults(), ...employee });
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isProbationaryPeriodExtended: false,
      hasDisabledChild: false,
      isFirstTimeAitGiver: false,
      isSalaryHold: false,
      isFestivalBonusHold: false,
      isPhysicallyDisabled: false,
      isFreedomFighter: false,
      hasOverTime: false,
      isTaxPaidByOrganisation: false,
      createdAt: currentTime,
      isAllowedToGiveOnlineAttendance: false,
      isFixedTermContract: false,
      currentInTime: currentTime,
      currentOutTime: currentTime,
      onlineAttendanceSanctionedAt: currentTime,
      isNidVerified: false,
      canRaiseRrfOnBehalf: false,
      canManageTaxAcknowledgementReceipt: false,
      isEligibleForAutomatedAttendance: false,
      isBillableResource: false,
      isAugmentedResource: false,
    };
  }

  private convertEmployeeRawValueToEmployee(rawEmployee: EmployeeFormRawValue | NewEmployeeFormRawValue): IEmployee | NewEmployee {
    return {
      ...rawEmployee,
      createdAt: dayjs(rawEmployee.createdAt, DATE_TIME_FORMAT),
      currentInTime: dayjs(rawEmployee.currentInTime, DATE_TIME_FORMAT),
      currentOutTime: dayjs(rawEmployee.currentOutTime, DATE_TIME_FORMAT),
      onlineAttendanceSanctionedAt: dayjs(rawEmployee.onlineAttendanceSanctionedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeToEmployeeRawValue(
    employee: IEmployee | (Partial<NewEmployee> & EmployeeFormDefaults)
  ): EmployeeFormRawValue | PartialWithRequiredKeyOf<NewEmployeeFormRawValue> {
    return {
      ...employee,
      createdAt: employee.createdAt ? employee.createdAt.format(DATE_TIME_FORMAT) : undefined,
      currentInTime: employee.currentInTime ? employee.currentInTime.format(DATE_TIME_FORMAT) : undefined,
      currentOutTime: employee.currentOutTime ? employee.currentOutTime.format(DATE_TIME_FORMAT) : undefined,
      onlineAttendanceSanctionedAt: employee.onlineAttendanceSanctionedAt
        ? employee.onlineAttendanceSanctionedAt.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
