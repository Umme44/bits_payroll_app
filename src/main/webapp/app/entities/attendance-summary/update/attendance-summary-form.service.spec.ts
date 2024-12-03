import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attendance-summary.test-samples';

import { AttendanceSummaryFormService } from './attendance-summary-form.service';

describe('AttendanceSummary Form Service', () => {
  let service: AttendanceSummaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendanceSummaryFormService);
  });

  describe('Service methods', () => {
    describe('createAttendanceSummaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttendanceSummaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            totalWorkingDays: expect.any(Object),
            totalLeaveDays: expect.any(Object),
            totalAbsentDays: expect.any(Object),
            totalFractionDays: expect.any(Object),
            attendanceRegularisationStartDate: expect.any(Object),
            attendanceRegularisationEndDate: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IAttendanceSummary should create a new form with FormGroup', () => {
        const formGroup = service.createAttendanceSummaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            totalWorkingDays: expect.any(Object),
            totalLeaveDays: expect.any(Object),
            totalAbsentDays: expect.any(Object),
            totalFractionDays: expect.any(Object),
            attendanceRegularisationStartDate: expect.any(Object),
            attendanceRegularisationEndDate: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getAttendanceSummary', () => {
      it('should return NewAttendanceSummary for default AttendanceSummary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAttendanceSummaryFormGroup(sampleWithNewData);

        const attendanceSummary = service.getAttendanceSummary(formGroup) as any;

        expect(attendanceSummary).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttendanceSummary for empty AttendanceSummary initial value', () => {
        const formGroup = service.createAttendanceSummaryFormGroup();

        const attendanceSummary = service.getAttendanceSummary(formGroup) as any;

        expect(attendanceSummary).toMatchObject({});
      });

      it('should return IAttendanceSummary', () => {
        const formGroup = service.createAttendanceSummaryFormGroup(sampleWithRequiredData);

        const attendanceSummary = service.getAttendanceSummary(formGroup) as any;

        expect(attendanceSummary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttendanceSummary should not enable id FormControl', () => {
        const formGroup = service.createAttendanceSummaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttendanceSummary should disable id FormControl', () => {
        const formGroup = service.createAttendanceSummaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
