import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../flex-schedule.test-samples';

import { FlexScheduleFormService } from './flex-schedule-form.service';

describe('FlexSchedule Form Service', () => {
  let service: FlexScheduleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlexScheduleFormService);
  });

  describe('Service methods', () => {
    describe('createFlexScheduleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFlexScheduleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveDate: expect.any(Object),
            inTime: expect.any(Object),
            outTime: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });

      it('passing IFlexSchedule should create a new form with FormGroup', () => {
        const formGroup = service.createFlexScheduleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveDate: expect.any(Object),
            inTime: expect.any(Object),
            outTime: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });
    });

    describe('getFlexSchedule', () => {
      it('should return NewFlexSchedule for default FlexSchedule initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFlexScheduleFormGroup(sampleWithNewData);

        const flexSchedule = service.getFlexSchedule(formGroup) as any;

        expect(flexSchedule).toMatchObject(sampleWithNewData);
      });

      it('should return NewFlexSchedule for empty FlexSchedule initial value', () => {
        const formGroup = service.createFlexScheduleFormGroup();

        const flexSchedule = service.getFlexSchedule(formGroup) as any;

        expect(flexSchedule).toMatchObject({});
      });

      it('should return IFlexSchedule', () => {
        const formGroup = service.createFlexScheduleFormGroup(sampleWithRequiredData);

        const flexSchedule = service.getFlexSchedule(formGroup) as any;

        expect(flexSchedule).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFlexSchedule should not enable id FormControl', () => {
        const formGroup = service.createFlexScheduleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFlexSchedule should disable id FormControl', () => {
        const formGroup = service.createFlexScheduleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
