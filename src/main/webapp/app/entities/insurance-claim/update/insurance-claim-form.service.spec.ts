import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../insurance-claim.test-samples';

import { InsuranceClaimFormService } from './insurance-claim-form.service';

describe('InsuranceClaim Form Service', () => {
  let service: InsuranceClaimFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsuranceClaimFormService);
  });

  describe('Service methods', () => {
    describe('createInsuranceClaimFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsuranceClaimFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            settlementDate: expect.any(Object),
            paymentDate: expect.any(Object),
            regretDate: expect.any(Object),
            regretReason: expect.any(Object),
            claimedAmount: expect.any(Object),
            settledAmount: expect.any(Object),
            claimStatus: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            insuranceRegistration: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IInsuranceClaim should create a new form with FormGroup', () => {
        const formGroup = service.createInsuranceClaimFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            settlementDate: expect.any(Object),
            paymentDate: expect.any(Object),
            regretDate: expect.any(Object),
            regretReason: expect.any(Object),
            claimedAmount: expect.any(Object),
            settledAmount: expect.any(Object),
            claimStatus: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            insuranceRegistration: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getInsuranceClaim', () => {
      it('should return NewInsuranceClaim for default InsuranceClaim initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInsuranceClaimFormGroup(sampleWithNewData);

        const insuranceClaim = service.getInsuranceClaim(formGroup) as any;

        expect(insuranceClaim).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsuranceClaim for empty InsuranceClaim initial value', () => {
        const formGroup = service.createInsuranceClaimFormGroup();

        const insuranceClaim = service.getInsuranceClaim(formGroup) as any;

        expect(insuranceClaim).toMatchObject({});
      });

      it('should return IInsuranceClaim', () => {
        const formGroup = service.createInsuranceClaimFormGroup(sampleWithRequiredData);

        const insuranceClaim = service.getInsuranceClaim(formGroup) as any;

        expect(insuranceClaim).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsuranceClaim should not enable id FormControl', () => {
        const formGroup = service.createInsuranceClaimFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsuranceClaim should disable id FormControl', () => {
        const formGroup = service.createInsuranceClaimFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
