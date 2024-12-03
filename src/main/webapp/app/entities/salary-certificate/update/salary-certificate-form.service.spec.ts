import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../salary-certificate.test-samples';

import { SalaryCertificateFormService } from './salary-certificate-form.service';

describe('SalaryCertificate Form Service', () => {
  let service: SalaryCertificateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryCertificateFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryCertificateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryCertificateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purpose: expect.any(Object),
            remarks: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            referenceNumber: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionBy: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
          })
        );
      });

      it('passing ISalaryCertificate should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryCertificateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purpose: expect.any(Object),
            remarks: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            referenceNumber: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionBy: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
          })
        );
      });
    });

    describe('getSalaryCertificate', () => {
      it('should return NewSalaryCertificate for default SalaryCertificate initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSalaryCertificateFormGroup(sampleWithNewData);

        const salaryCertificate = service.getSalaryCertificate(formGroup) as any;

        expect(salaryCertificate).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalaryCertificate for empty SalaryCertificate initial value', () => {
        const formGroup = service.createSalaryCertificateFormGroup();

        const salaryCertificate = service.getSalaryCertificate(formGroup) as any;

        expect(salaryCertificate).toMatchObject({});
      });

      it('should return ISalaryCertificate', () => {
        const formGroup = service.createSalaryCertificateFormGroup(sampleWithRequiredData);

        const salaryCertificate = service.getSalaryCertificate(formGroup) as any;

        expect(salaryCertificate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalaryCertificate should not enable id FormControl', () => {
        const formGroup = service.createSalaryCertificateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalaryCertificate should disable id FormControl', () => {
        const formGroup = service.createSalaryCertificateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
