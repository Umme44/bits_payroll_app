import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employment-history.test-samples';

import { EmploymentHistoryFormService } from './employment-history-form.service';

describe('EmploymentHistory Form Service', () => {
  let service: EmploymentHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmploymentHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createEmploymentHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmploymentHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceId: expect.any(Object),
            pin: expect.any(Object),
            eventType: expect.any(Object),
            effectiveDate: expect.any(Object),
            previousMainGrossSalary: expect.any(Object),
            currentMainGrossSalary: expect.any(Object),
            previousWorkingHour: expect.any(Object),
            changedWorkingHour: expect.any(Object),
            isModifiable: expect.any(Object),
            previousDesignation: expect.any(Object),
            changedDesignation: expect.any(Object),
            previousDepartment: expect.any(Object),
            changedDepartment: expect.any(Object),
            previousReportingTo: expect.any(Object),
            changedReportingTo: expect.any(Object),
            employee: expect.any(Object),
            previousUnit: expect.any(Object),
            changedUnit: expect.any(Object),
            previousBand: expect.any(Object),
            changedBand: expect.any(Object),
          })
        );
      });

      it('passing IEmploymentHistory should create a new form with FormGroup', () => {
        const formGroup = service.createEmploymentHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceId: expect.any(Object),
            pin: expect.any(Object),
            eventType: expect.any(Object),
            effectiveDate: expect.any(Object),
            previousMainGrossSalary: expect.any(Object),
            currentMainGrossSalary: expect.any(Object),
            previousWorkingHour: expect.any(Object),
            changedWorkingHour: expect.any(Object),
            isModifiable: expect.any(Object),
            previousDesignation: expect.any(Object),
            changedDesignation: expect.any(Object),
            previousDepartment: expect.any(Object),
            changedDepartment: expect.any(Object),
            previousReportingTo: expect.any(Object),
            changedReportingTo: expect.any(Object),
            employee: expect.any(Object),
            previousUnit: expect.any(Object),
            changedUnit: expect.any(Object),
            previousBand: expect.any(Object),
            changedBand: expect.any(Object),
          })
        );
      });
    });

    describe('getEmploymentHistory', () => {
      it('should return NewEmploymentHistory for default EmploymentHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmploymentHistoryFormGroup(sampleWithNewData);

        const employmentHistory = service.getEmploymentHistory(formGroup) as any;

        expect(employmentHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmploymentHistory for empty EmploymentHistory initial value', () => {
        const formGroup = service.createEmploymentHistoryFormGroup();

        const employmentHistory = service.getEmploymentHistory(formGroup) as any;

        expect(employmentHistory).toMatchObject({});
      });

      it('should return IEmploymentHistory', () => {
        const formGroup = service.createEmploymentHistoryFormGroup(sampleWithRequiredData);

        const employmentHistory = service.getEmploymentHistory(formGroup) as any;

        expect(employmentHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmploymentHistory should not enable id FormControl', () => {
        const formGroup = service.createEmploymentHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmploymentHistory should disable id FormControl', () => {
        const formGroup = service.createEmploymentHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
