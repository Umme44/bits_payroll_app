import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../event-log.test-samples';

import { EventLogFormService } from './event-log-form.service';

describe('EventLog Form Service', () => {
  let service: EventLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventLogFormService);
  });

  describe('Service methods', () => {
    describe('createEventLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            requestMethod: expect.any(Object),
            performedAt: expect.any(Object),
            data: expect.any(Object),
            entityName: expect.any(Object),
            performedBy: expect.any(Object),
          })
        );
      });

      it('passing IEventLog should create a new form with FormGroup', () => {
        const formGroup = service.createEventLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            requestMethod: expect.any(Object),
            performedAt: expect.any(Object),
            data: expect.any(Object),
            entityName: expect.any(Object),
            performedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getEventLog', () => {
      it('should return NewEventLog for default EventLog initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEventLogFormGroup(sampleWithNewData);

        const eventLog = service.getEventLog(formGroup) as any;

        expect(eventLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventLog for empty EventLog initial value', () => {
        const formGroup = service.createEventLogFormGroup();

        const eventLog = service.getEventLog(formGroup) as any;

        expect(eventLog).toMatchObject({});
      });

      it('should return IEventLog', () => {
        const formGroup = service.createEventLogFormGroup(sampleWithRequiredData);

        const eventLog = service.getEventLog(formGroup) as any;

        expect(eventLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventLog should not enable id FormControl', () => {
        const formGroup = service.createEventLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventLog should disable id FormControl', () => {
        const formGroup = service.createEventLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
