import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-pin-configuration.test-samples';

import { EmployeePinConfigurationFormService } from './employee-pin-configuration-form.service';

describe('EmployeePinConfiguration Form Service', () => {
  let service: EmployeePinConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeePinConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeePinConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeCategory: expect.any(Object),
            sequenceStart: expect.any(Object),
            sequenceEnd: expect.any(Object),
            lastSequence: expect.any(Object),
            hasFullFilled: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            lastCreatedPin: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IEmployeePinConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeCategory: expect.any(Object),
            sequenceStart: expect.any(Object),
            sequenceEnd: expect.any(Object),
            lastSequence: expect.any(Object),
            hasFullFilled: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            lastCreatedPin: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeePinConfiguration', () => {
      it('should return NewEmployeePinConfiguration for default EmployeePinConfiguration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeePinConfigurationFormGroup(sampleWithNewData);

        const employeePinConfiguration = service.getEmployeePinConfiguration(formGroup) as any;

        expect(employeePinConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeePinConfiguration for empty EmployeePinConfiguration initial value', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup();

        const employeePinConfiguration = service.getEmployeePinConfiguration(formGroup) as any;

        expect(employeePinConfiguration).toMatchObject({});
      });

      it('should return IEmployeePinConfiguration', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup(sampleWithRequiredData);

        const employeePinConfiguration = service.getEmployeePinConfiguration(formGroup) as any;

        expect(employeePinConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeePinConfiguration should not enable id FormControl', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeePinConfiguration should disable id FormControl', () => {
        const formGroup = service.createEmployeePinConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
