import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-document.test-samples';

import { EmployeeDocumentFormService } from './employee-document-form.service';

describe('EmployeeDocument Form Service', () => {
  let service: EmployeeDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            fileName: expect.any(Object),
            filePath: expect.any(Object),
            hasEmployeeVisibility: expect.any(Object),
            remarks: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            fileExtension: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeDocument should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            fileName: expect.any(Object),
            filePath: expect.any(Object),
            hasEmployeeVisibility: expect.any(Object),
            remarks: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            fileExtension: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeDocument', () => {
      it('should return NewEmployeeDocument for default EmployeeDocument initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeDocumentFormGroup(sampleWithNewData);

        const employeeDocument = service.getEmployeeDocument(formGroup) as any;

        expect(employeeDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeDocument for empty EmployeeDocument initial value', () => {
        const formGroup = service.createEmployeeDocumentFormGroup();

        const employeeDocument = service.getEmployeeDocument(formGroup) as any;

        expect(employeeDocument).toMatchObject({});
      });

      it('should return IEmployeeDocument', () => {
        const formGroup = service.createEmployeeDocumentFormGroup(sampleWithRequiredData);

        const employeeDocument = service.getEmployeeDocument(formGroup) as any;

        expect(employeeDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeDocument should not enable id FormControl', () => {
        const formGroup = service.createEmployeeDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeDocument should disable id FormControl', () => {
        const formGroup = service.createEmployeeDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
