import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../flex-schedule-application.test-samples';

import { FlexScheduleApplicationFormService } from './flex-schedule-application-form.service';

describe('FlexScheduleApplication Form Service', () => {
  let service: FlexScheduleApplicationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlexScheduleApplicationFormService);
  });

  describe('Service methods', () => {
    describe('createFlexScheduleApplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveFrom: expect.any(Object),
            effectiveTo: expect.any(Object),
            reason: expect.any(Object),
            status: expect.any(Object),
            sanctionedAt: expect.any(Object),
            appliedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            requester: expect.any(Object),
            sanctionedBy: expect.any(Object),
            appliedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            timeSlot: expect.any(Object),
          })
        );
      });

      it('passing IFlexScheduleApplication should create a new form with FormGroup', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveFrom: expect.any(Object),
            effectiveTo: expect.any(Object),
            reason: expect.any(Object),
            status: expect.any(Object),
            sanctionedAt: expect.any(Object),
            appliedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            requester: expect.any(Object),
            sanctionedBy: expect.any(Object),
            appliedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            timeSlot: expect.any(Object),
          })
        );
      });
    });

    describe('getFlexScheduleApplication', () => {
      it('should return NewFlexScheduleApplication for default FlexScheduleApplication initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFlexScheduleApplicationFormGroup(sampleWithNewData);

        const flexScheduleApplication = service.getFlexScheduleApplication(formGroup) as any;

        expect(flexScheduleApplication).toMatchObject(sampleWithNewData);
      });

      it('should return NewFlexScheduleApplication for empty FlexScheduleApplication initial value', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup();

        const flexScheduleApplication = service.getFlexScheduleApplication(formGroup) as any;

        expect(flexScheduleApplication).toMatchObject({});
      });

      it('should return IFlexScheduleApplication', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup(sampleWithRequiredData);

        const flexScheduleApplication = service.getFlexScheduleApplication(formGroup) as any;

        expect(flexScheduleApplication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFlexScheduleApplication should not enable id FormControl', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFlexScheduleApplication should disable id FormControl', () => {
        const formGroup = service.createFlexScheduleApplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
