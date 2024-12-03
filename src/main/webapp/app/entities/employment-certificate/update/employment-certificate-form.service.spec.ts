import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employment-certificate.test-samples';

import { EmploymentCertificateFormService } from './employment-certificate-form.service';

describe('EmploymentCertificate Form Service', () => {
  let service: EmploymentCertificateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmploymentCertificateFormService);
  });

  describe('Service methods', () => {
    describe('createEmploymentCertificateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmploymentCertificateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            certificateStatus: expect.any(Object),
            referenceNumber: expect.any(Object),
            issueDate: expect.any(Object),
            reason: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            generatedAt: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            generatedBy: expect.any(Object),
          })
        );
      });

      it('passing IEmploymentCertificate should create a new form with FormGroup', () => {
        const formGroup = service.createEmploymentCertificateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            certificateStatus: expect.any(Object),
            referenceNumber: expect.any(Object),
            issueDate: expect.any(Object),
            reason: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            generatedAt: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            generatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getEmploymentCertificate', () => {
      it('should return NewEmploymentCertificate for default EmploymentCertificate initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmploymentCertificateFormGroup(sampleWithNewData);

        const employmentCertificate = service.getEmploymentCertificate(formGroup) as any;

        expect(employmentCertificate).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmploymentCertificate for empty EmploymentCertificate initial value', () => {
        const formGroup = service.createEmploymentCertificateFormGroup();

        const employmentCertificate = service.getEmploymentCertificate(formGroup) as any;

        expect(employmentCertificate).toMatchObject({});
      });

      it('should return IEmploymentCertificate', () => {
        const formGroup = service.createEmploymentCertificateFormGroup(sampleWithRequiredData);

        const employmentCertificate = service.getEmploymentCertificate(formGroup) as any;

        expect(employmentCertificate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmploymentCertificate should not enable id FormControl', () => {
        const formGroup = service.createEmploymentCertificateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmploymentCertificate should disable id FormControl', () => {
        const formGroup = service.createEmploymentCertificateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
