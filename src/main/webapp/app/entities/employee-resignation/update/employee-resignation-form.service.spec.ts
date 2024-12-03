import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-resignation.test-samples';

import { EmployeeResignationFormService } from './employee-resignation-form.service';

describe('EmployeeResignation Form Service', () => {
  let service: EmployeeResignationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeResignationFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeResignationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeResignationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            resignationDate: expect.any(Object),
            approvalStatus: expect.any(Object),
            approvalComment: expect.any(Object),
            isSalaryHold: expect.any(Object),
            isFestivalBonusHold: expect.any(Object),
            resignationReason: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            createdBy: expect.any(Object),
            uodatedBy: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeResignation should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeResignationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            resignationDate: expect.any(Object),
            approvalStatus: expect.any(Object),
            approvalComment: expect.any(Object),
            isSalaryHold: expect.any(Object),
            isFestivalBonusHold: expect.any(Object),
            resignationReason: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            createdBy: expect.any(Object),
            uodatedBy: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeResignation', () => {
      it('should return NewEmployeeResignation for default EmployeeResignation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeResignationFormGroup(sampleWithNewData);

        const employeeResignation = service.getEmployeeResignation(formGroup) as any;

        expect(employeeResignation).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeResignation for empty EmployeeResignation initial value', () => {
        const formGroup = service.createEmployeeResignationFormGroup();

        const employeeResignation = service.getEmployeeResignation(formGroup) as any;

        expect(employeeResignation).toMatchObject({});
      });

      it('should return IEmployeeResignation', () => {
        const formGroup = service.createEmployeeResignationFormGroup(sampleWithRequiredData);

        const employeeResignation = service.getEmployeeResignation(formGroup) as any;

        expect(employeeResignation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeResignation should not enable id FormControl', () => {
        const formGroup = service.createEmployeeResignationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeResignation should disable id FormControl', () => {
        const formGroup = service.createEmployeeResignationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
