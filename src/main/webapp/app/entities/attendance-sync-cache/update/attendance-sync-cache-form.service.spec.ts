import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attendance-sync-cache.test-samples';

import { AttendanceSyncCacheFormService } from './attendance-sync-cache-form.service';

describe('AttendanceSyncCache Form Service', () => {
  let service: AttendanceSyncCacheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendanceSyncCacheFormService);
  });

  describe('Service methods', () => {
    describe('createAttendanceSyncCacheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeePin: expect.any(Object),
            timestamp: expect.any(Object),
            terminal: expect.any(Object),
          })
        );
      });

      it('passing IAttendanceSyncCache should create a new form with FormGroup', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeePin: expect.any(Object),
            timestamp: expect.any(Object),
            terminal: expect.any(Object),
          })
        );
      });
    });

    describe('getAttendanceSyncCache', () => {
      it('should return NewAttendanceSyncCache for default AttendanceSyncCache initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAttendanceSyncCacheFormGroup(sampleWithNewData);

        const attendanceSyncCache = service.getAttendanceSyncCache(formGroup) as any;

        expect(attendanceSyncCache).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttendanceSyncCache for empty AttendanceSyncCache initial value', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup();

        const attendanceSyncCache = service.getAttendanceSyncCache(formGroup) as any;

        expect(attendanceSyncCache).toMatchObject({});
      });

      it('should return IAttendanceSyncCache', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup(sampleWithRequiredData);

        const attendanceSyncCache = service.getAttendanceSyncCache(formGroup) as any;

        expect(attendanceSyncCache).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttendanceSyncCache should not enable id FormControl', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttendanceSyncCache should disable id FormControl', () => {
        const formGroup = service.createAttendanceSyncCacheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
