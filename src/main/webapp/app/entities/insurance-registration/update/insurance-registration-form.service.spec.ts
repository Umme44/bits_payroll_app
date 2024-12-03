import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../insurance-registration.test-samples';

import { InsuranceRegistrationFormService } from './insurance-registration-form.service';

describe('InsuranceRegistration Form Service', () => {
  let service: InsuranceRegistrationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsuranceRegistrationFormService);
  });

  describe('Service methods', () => {
    describe('createInsuranceRegistrationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            photo: expect.any(Object),
            insuranceRelation: expect.any(Object),
            insuranceStatus: expect.any(Object),
            unapprovalReason: expect.any(Object),
            availableBalance: expect.any(Object),
            updatedAt: expect.any(Object),
            approvedAt: expect.any(Object),
            insuranceId: expect.any(Object),
            createdAt: expect.any(Object),
            employee: expect.any(Object),
            approvedBy: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });

      it('passing IInsuranceRegistration should create a new form with FormGroup', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            photo: expect.any(Object),
            insuranceRelation: expect.any(Object),
            insuranceStatus: expect.any(Object),
            unapprovalReason: expect.any(Object),
            availableBalance: expect.any(Object),
            updatedAt: expect.any(Object),
            approvedAt: expect.any(Object),
            insuranceId: expect.any(Object),
            createdAt: expect.any(Object),
            employee: expect.any(Object),
            approvedBy: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });
    });

    describe('getInsuranceRegistration', () => {
      it('should return NewInsuranceRegistration for default InsuranceRegistration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInsuranceRegistrationFormGroup(sampleWithNewData);

        const insuranceRegistration = service.getInsuranceRegistration(formGroup) as any;

        expect(insuranceRegistration).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsuranceRegistration for empty InsuranceRegistration initial value', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup();

        const insuranceRegistration = service.getInsuranceRegistration(formGroup) as any;

        expect(insuranceRegistration).toMatchObject({});
      });

      it('should return IInsuranceRegistration', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup(sampleWithRequiredData);

        const insuranceRegistration = service.getInsuranceRegistration(formGroup) as any;

        expect(insuranceRegistration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsuranceRegistration should not enable id FormControl', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsuranceRegistration should disable id FormControl', () => {
        const formGroup = service.createInsuranceRegistrationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
