import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../special-shift-timing.test-samples';

import { SpecialShiftTimingFormService } from './special-shift-timing-form.service';

describe('SpecialShiftTiming Form Service', () => {
  let service: SpecialShiftTimingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpecialShiftTimingFormService);
  });

  describe('Service methods', () => {
    describe('createSpecialShiftTimingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            overrideRoaster: expect.any(Object),
            overrideFlexSchedule: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            reason: expect.any(Object),
            timeSlot: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing ISpecialShiftTiming should create a new form with FormGroup', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            overrideRoaster: expect.any(Object),
            overrideFlexSchedule: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            reason: expect.any(Object),
            timeSlot: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getSpecialShiftTiming', () => {
      it('should return NewSpecialShiftTiming for default SpecialShiftTiming initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSpecialShiftTimingFormGroup(sampleWithNewData);

        const specialShiftTiming = service.getSpecialShiftTiming(formGroup) as any;

        expect(specialShiftTiming).toMatchObject(sampleWithNewData);
      });

      it('should return NewSpecialShiftTiming for empty SpecialShiftTiming initial value', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup();

        const specialShiftTiming = service.getSpecialShiftTiming(formGroup) as any;

        expect(specialShiftTiming).toMatchObject({});
      });

      it('should return ISpecialShiftTiming', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup(sampleWithRequiredData);

        const specialShiftTiming = service.getSpecialShiftTiming(formGroup) as any;

        expect(specialShiftTiming).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISpecialShiftTiming should not enable id FormControl', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSpecialShiftTiming should disable id FormControl', () => {
        const formGroup = service.createSpecialShiftTimingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
