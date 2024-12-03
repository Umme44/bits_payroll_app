import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-pin.test-samples';

import { EmployeePinFormService } from './employee-pin-form.service';

describe('EmployeePin Form Service', () => {
  let service: EmployeePinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeePinFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeePinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeePinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            fullName: expect.any(Object),
            employeeCategory: expect.any(Object),
            employeePinStatus: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            designation: expect.any(Object),
            unit: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
            recruitmentRequisitionForm: expect.any(Object),
          })
        );
      });

      it('passing IEmployeePin should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeePinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            fullName: expect.any(Object),
            employeeCategory: expect.any(Object),
            employeePinStatus: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            designation: expect.any(Object),
            unit: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
            recruitmentRequisitionForm: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeePin', () => {
      it('should return NewEmployeePin for default EmployeePin initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeePinFormGroup(sampleWithNewData);

        const employeePin = service.getEmployeePin(formGroup) as any;

        expect(employeePin).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeePin for empty EmployeePin initial value', () => {
        const formGroup = service.createEmployeePinFormGroup();

        const employeePin = service.getEmployeePin(formGroup) as any;

        expect(employeePin).toMatchObject({});
      });

      it('should return IEmployeePin', () => {
        const formGroup = service.createEmployeePinFormGroup(sampleWithRequiredData);

        const employeePin = service.getEmployeePin(formGroup) as any;

        expect(employeePin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeePin should not enable id FormControl', () => {
        const formGroup = service.createEmployeePinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeePin should disable id FormControl', () => {
        const formGroup = service.createEmployeePinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
