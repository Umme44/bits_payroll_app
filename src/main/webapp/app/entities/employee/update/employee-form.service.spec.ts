import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee.test-samples';

import { EmployeeFormService } from './employee-form.service';

describe('Employee Form Service', () => {
  let service: EmployeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceId: expect.any(Object),
            pin: expect.any(Object),
            picture: expect.any(Object),
            fullName: expect.any(Object),
            surName: expect.any(Object),
            nationalIdNo: expect.any(Object),
            dateOfBirth: expect.any(Object),
            placeOfBirth: expect.any(Object),
            fatherName: expect.any(Object),
            motherName: expect.any(Object),
            bloodGroup: expect.any(Object),
            presentAddress: expect.any(Object),
            permanentAddress: expect.any(Object),
            personalContactNo: expect.any(Object),
            personalEmail: expect.any(Object),
            religion: expect.any(Object),
            maritalStatus: expect.any(Object),
            dateOfMarriage: expect.any(Object),
            spouseName: expect.any(Object),
            officialEmail: expect.any(Object),
            officialContactNo: expect.any(Object),
            officePhoneExtension: expect.any(Object),
            whatsappId: expect.any(Object),
            skypeId: expect.any(Object),
            emergencyContactPersonName: expect.any(Object),
            emergencyContactPersonRelationshipWithEmployee: expect.any(Object),
            emergencyContactPersonContactNumber: expect.any(Object),
            mainGrossSalary: expect.any(Object),
            employeeCategory: expect.any(Object),
            location: expect.any(Object),
            dateOfJoining: expect.any(Object),
            dateOfConfirmation: expect.any(Object),
            isProbationaryPeriodExtended: expect.any(Object),
            probationPeriodExtendedTo: expect.any(Object),
            payType: expect.any(Object),
            disbursementMethod: expect.any(Object),
            bankName: expect.any(Object),
            bankAccountNo: expect.any(Object),
            mobileCelling: expect.any(Object),
            bkashNumber: expect.any(Object),
            cardType: expect.any(Object),
            cardNumber: expect.any(Object),
            tinNumber: expect.any(Object),
            passportNo: expect.any(Object),
            passportPlaceOfIssue: expect.any(Object),
            passportIssuedDate: expect.any(Object),
            passportExpiryDate: expect.any(Object),
            gender: expect.any(Object),
            welfareFundDeduction: expect.any(Object),
            employmentStatus: expect.any(Object),
            hasDisabledChild: expect.any(Object),
            isFirstTimeAitGiver: expect.any(Object),
            isSalaryHold: expect.any(Object),
            isFestivalBonusHold: expect.any(Object),
            isPhysicallyDisabled: expect.any(Object),
            isFreedomFighter: expect.any(Object),
            hasOverTime: expect.any(Object),
            probationPeriodEndDate: expect.any(Object),
            contractPeriodExtendedTo: expect.any(Object),
            contractPeriodEndDate: expect.any(Object),
            cardType02: expect.any(Object),
            cardNumber02: expect.any(Object),
            cardType03: expect.any(Object),
            cardNumber03: expect.any(Object),
            allowance01: expect.any(Object),
            allowance01EffectiveFrom: expect.any(Object),
            allowance01EffectiveTo: expect.any(Object),
            allowance02: expect.any(Object),
            allowance02EffectiveFrom: expect.any(Object),
            allowance02EffectiveTo: expect.any(Object),
            allowance03: expect.any(Object),
            allowance03EffectiveFrom: expect.any(Object),
            allowance03EffectiveTo: expect.any(Object),
            allowance04: expect.any(Object),
            allowance04EffectiveFrom: expect.any(Object),
            allowance04EffectiveTo: expect.any(Object),
            allowance05: expect.any(Object),
            allowance05EffectiveFrom: expect.any(Object),
            allowance05EffectiveTo: expect.any(Object),
            allowance06: expect.any(Object),
            allowance06EffectiveFrom: expect.any(Object),
            allowance06EffectiveTo: expect.any(Object),
            isTaxPaidByOrganisation: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            isAllowedToGiveOnlineAttendance: expect.any(Object),
            noticePeriodInDays: expect.any(Object),
            isFixedTermContract: expect.any(Object),
            currentInTime: expect.any(Object),
            currentOutTime: expect.any(Object),
            onlineAttendanceSanctionedAt: expect.any(Object),
            isNidVerified: expect.any(Object),
            canRaiseRrfOnBehalf: expect.any(Object),
            taxesCircle: expect.any(Object),
            taxesZone: expect.any(Object),
            canManageTaxAcknowledgementReceipt: expect.any(Object),
            isEligibleForAutomatedAttendance: expect.any(Object),
            isFestivalBonusDisabled: expect.any(Object),
            isCurrentlyResigned: expect.any(Object),
            floor: expect.any(Object),
            deskLocation: expect.any(Object),
            isBillableResource: expect.any(Object),
            isAugmentedResource: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            officeLocation: expect.any(Object),
            designation: expect.any(Object),
            department: expect.any(Object),
            reportingTo: expect.any(Object),
            nationality: expect.any(Object),
            bankBranch: expect.any(Object),
            band: expect.any(Object),
            unit: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IEmployee should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceId: expect.any(Object),
            pin: expect.any(Object),
            picture: expect.any(Object),
            fullName: expect.any(Object),
            surName: expect.any(Object),
            nationalIdNo: expect.any(Object),
            dateOfBirth: expect.any(Object),
            placeOfBirth: expect.any(Object),
            fatherName: expect.any(Object),
            motherName: expect.any(Object),
            bloodGroup: expect.any(Object),
            presentAddress: expect.any(Object),
            permanentAddress: expect.any(Object),
            personalContactNo: expect.any(Object),
            personalEmail: expect.any(Object),
            religion: expect.any(Object),
            maritalStatus: expect.any(Object),
            dateOfMarriage: expect.any(Object),
            spouseName: expect.any(Object),
            officialEmail: expect.any(Object),
            officialContactNo: expect.any(Object),
            officePhoneExtension: expect.any(Object),
            whatsappId: expect.any(Object),
            skypeId: expect.any(Object),
            emergencyContactPersonName: expect.any(Object),
            emergencyContactPersonRelationshipWithEmployee: expect.any(Object),
            emergencyContactPersonContactNumber: expect.any(Object),
            mainGrossSalary: expect.any(Object),
            employeeCategory: expect.any(Object),
            location: expect.any(Object),
            dateOfJoining: expect.any(Object),
            dateOfConfirmation: expect.any(Object),
            isProbationaryPeriodExtended: expect.any(Object),
            probationPeriodExtendedTo: expect.any(Object),
            payType: expect.any(Object),
            disbursementMethod: expect.any(Object),
            bankName: expect.any(Object),
            bankAccountNo: expect.any(Object),
            mobileCelling: expect.any(Object),
            bkashNumber: expect.any(Object),
            cardType: expect.any(Object),
            cardNumber: expect.any(Object),
            tinNumber: expect.any(Object),
            passportNo: expect.any(Object),
            passportPlaceOfIssue: expect.any(Object),
            passportIssuedDate: expect.any(Object),
            passportExpiryDate: expect.any(Object),
            gender: expect.any(Object),
            welfareFundDeduction: expect.any(Object),
            employmentStatus: expect.any(Object),
            hasDisabledChild: expect.any(Object),
            isFirstTimeAitGiver: expect.any(Object),
            isSalaryHold: expect.any(Object),
            isFestivalBonusHold: expect.any(Object),
            isPhysicallyDisabled: expect.any(Object),
            isFreedomFighter: expect.any(Object),
            hasOverTime: expect.any(Object),
            probationPeriodEndDate: expect.any(Object),
            contractPeriodExtendedTo: expect.any(Object),
            contractPeriodEndDate: expect.any(Object),
            cardType02: expect.any(Object),
            cardNumber02: expect.any(Object),
            cardType03: expect.any(Object),
            cardNumber03: expect.any(Object),
            allowance01: expect.any(Object),
            allowance01EffectiveFrom: expect.any(Object),
            allowance01EffectiveTo: expect.any(Object),
            allowance02: expect.any(Object),
            allowance02EffectiveFrom: expect.any(Object),
            allowance02EffectiveTo: expect.any(Object),
            allowance03: expect.any(Object),
            allowance03EffectiveFrom: expect.any(Object),
            allowance03EffectiveTo: expect.any(Object),
            allowance04: expect.any(Object),
            allowance04EffectiveFrom: expect.any(Object),
            allowance04EffectiveTo: expect.any(Object),
            allowance05: expect.any(Object),
            allowance05EffectiveFrom: expect.any(Object),
            allowance05EffectiveTo: expect.any(Object),
            allowance06: expect.any(Object),
            allowance06EffectiveFrom: expect.any(Object),
            allowance06EffectiveTo: expect.any(Object),
            isTaxPaidByOrganisation: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            isAllowedToGiveOnlineAttendance: expect.any(Object),
            noticePeriodInDays: expect.any(Object),
            isFixedTermContract: expect.any(Object),
            currentInTime: expect.any(Object),
            currentOutTime: expect.any(Object),
            onlineAttendanceSanctionedAt: expect.any(Object),
            isNidVerified: expect.any(Object),
            canRaiseRrfOnBehalf: expect.any(Object),
            taxesCircle: expect.any(Object),
            taxesZone: expect.any(Object),
            canManageTaxAcknowledgementReceipt: expect.any(Object),
            isEligibleForAutomatedAttendance: expect.any(Object),
            isFestivalBonusDisabled: expect.any(Object),
            isCurrentlyResigned: expect.any(Object),
            floor: expect.any(Object),
            deskLocation: expect.any(Object),
            isBillableResource: expect.any(Object),
            isAugmentedResource: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            officeLocation: expect.any(Object),
            designation: expect.any(Object),
            department: expect.any(Object),
            reportingTo: expect.any(Object),
            nationality: expect.any(Object),
            bankBranch: expect.any(Object),
            band: expect.any(Object),
            unit: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployee', () => {
      it('should return NewEmployee for default Employee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeFormGroup(sampleWithNewData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployee for empty Employee initial value', () => {
        const formGroup = service.createEmployeeFormGroup();

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject({});
      });

      it('should return IEmployee', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployee should not enable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployee should disable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
