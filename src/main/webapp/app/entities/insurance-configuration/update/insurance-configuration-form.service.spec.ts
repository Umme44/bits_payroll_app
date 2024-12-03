import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../insurance-configuration.test-samples';

import { InsuranceConfigurationFormService } from './insurance-configuration-form.service';

describe('InsuranceConfiguration Form Service', () => {
  let service: InsuranceConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsuranceConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createInsuranceConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxTotalClaimLimitPerYear: expect.any(Object),
            maxAllowedChildAge: expect.any(Object),
            insuranceClaimLink: expect.any(Object),
          })
        );
      });

      it('passing IInsuranceConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxTotalClaimLimitPerYear: expect.any(Object),
            maxAllowedChildAge: expect.any(Object),
            insuranceClaimLink: expect.any(Object),
          })
        );
      });
    });

    describe('getInsuranceConfiguration', () => {
      it('should return NewInsuranceConfiguration for default InsuranceConfiguration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInsuranceConfigurationFormGroup(sampleWithNewData);

        const insuranceConfiguration = service.getInsuranceConfiguration(formGroup) as any;

        expect(insuranceConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsuranceConfiguration for empty InsuranceConfiguration initial value', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup();

        const insuranceConfiguration = service.getInsuranceConfiguration(formGroup) as any;

        expect(insuranceConfiguration).toMatchObject({});
      });

      it('should return IInsuranceConfiguration', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup(sampleWithRequiredData);

        const insuranceConfiguration = service.getInsuranceConfiguration(formGroup) as any;

        expect(insuranceConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsuranceConfiguration should not enable id FormControl', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsuranceConfiguration should disable id FormControl', () => {
        const formGroup = service.createInsuranceConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
