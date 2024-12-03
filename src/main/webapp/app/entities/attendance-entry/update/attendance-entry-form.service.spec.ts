import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attendance-entry.test-samples';

import { AttendanceEntryFormService } from './attendance-entry-form.service';

describe('AttendanceEntry Form Service', () => {
  let service: AttendanceEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendanceEntryFormService);
  });

  describe('Service methods', () => {
    describe('createAttendanceEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttendanceEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            inTime: expect.any(Object),
            inNote: expect.any(Object),
            outTime: expect.any(Object),
            outNote: expect.any(Object),
            status: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IAttendanceEntry should create a new form with FormGroup', () => {
        const formGroup = service.createAttendanceEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            inTime: expect.any(Object),
            inNote: expect.any(Object),
            outTime: expect.any(Object),
            outNote: expect.any(Object),
            status: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getAttendanceEntry', () => {
      it('should return NewAttendanceEntry for default AttendanceEntry initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAttendanceEntryFormGroup(sampleWithNewData);

        const attendanceEntry = service.getAttendanceEntry(formGroup) as any;

        expect(attendanceEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttendanceEntry for empty AttendanceEntry initial value', () => {
        const formGroup = service.createAttendanceEntryFormGroup();

        const attendanceEntry = service.getAttendanceEntry(formGroup) as any;

        expect(attendanceEntry).toMatchObject({});
      });

      it('should return IAttendanceEntry', () => {
        const formGroup = service.createAttendanceEntryFormGroup(sampleWithRequiredData);

        const attendanceEntry = service.getAttendanceEntry(formGroup) as any;

        expect(attendanceEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttendanceEntry should not enable id FormControl', () => {
        const formGroup = service.createAttendanceEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttendanceEntry should disable id FormControl', () => {
        const formGroup = service.createAttendanceEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
