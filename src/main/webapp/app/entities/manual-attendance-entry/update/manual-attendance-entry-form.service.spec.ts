import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../manual-attendance-entry.test-samples';

import { ManualAttendanceEntryFormService } from './manual-attendance-entry-form.service';

describe('ManualAttendanceEntry Form Service', () => {
  let service: ManualAttendanceEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManualAttendanceEntryFormService);
  });

  describe('Service methods', () => {
    describe('createManualAttendanceEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            inTime: expect.any(Object),
            inNote: expect.any(Object),
            outTime: expect.any(Object),
            outNote: expect.any(Object),
            isLineManagerApproved: expect.any(Object),
            isHRApproved: expect.any(Object),
            isRejected: expect.any(Object),
            rejectionComment: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IManualAttendanceEntry should create a new form with FormGroup', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            inTime: expect.any(Object),
            inNote: expect.any(Object),
            outTime: expect.any(Object),
            outNote: expect.any(Object),
            isLineManagerApproved: expect.any(Object),
            isHRApproved: expect.any(Object),
            isRejected: expect.any(Object),
            rejectionComment: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getManualAttendanceEntry', () => {
      it('should return NewManualAttendanceEntry for default ManualAttendanceEntry initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createManualAttendanceEntryFormGroup(sampleWithNewData);

        const manualAttendanceEntry = service.getManualAttendanceEntry(formGroup) as any;

        expect(manualAttendanceEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewManualAttendanceEntry for empty ManualAttendanceEntry initial value', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup();

        const manualAttendanceEntry = service.getManualAttendanceEntry(formGroup) as any;

        expect(manualAttendanceEntry).toMatchObject({});
      });

      it('should return IManualAttendanceEntry', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup(sampleWithRequiredData);

        const manualAttendanceEntry = service.getManualAttendanceEntry(formGroup) as any;

        expect(manualAttendanceEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IManualAttendanceEntry should not enable id FormControl', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewManualAttendanceEntry should disable id FormControl', () => {
        const formGroup = service.createManualAttendanceEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
