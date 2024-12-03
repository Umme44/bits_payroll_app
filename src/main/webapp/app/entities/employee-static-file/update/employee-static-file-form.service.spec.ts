import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-static-file.test-samples';

import { EmployeeStaticFileFormService } from './employee-static-file-form.service';

describe('EmployeeStaticFile Form Service', () => {
  let service: EmployeeStaticFileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeStaticFileFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeStaticFileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filePath: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeStaticFile should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filePath: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeStaticFile', () => {
      it('should return NewEmployeeStaticFile for default EmployeeStaticFile initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeStaticFileFormGroup(sampleWithNewData);

        const employeeStaticFile = service.getEmployeeStaticFile(formGroup) as any;

        expect(employeeStaticFile).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeStaticFile for empty EmployeeStaticFile initial value', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup();

        const employeeStaticFile = service.getEmployeeStaticFile(formGroup) as any;

        expect(employeeStaticFile).toMatchObject({});
      });

      it('should return IEmployeeStaticFile', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup(sampleWithRequiredData);

        const employeeStaticFile = service.getEmployeeStaticFile(formGroup) as any;

        expect(employeeStaticFile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeStaticFile should not enable id FormControl', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeStaticFile should disable id FormControl', () => {
        const formGroup = service.createEmployeeStaticFileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
