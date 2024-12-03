import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../movement-entry.test-samples';

import { MovementEntryFormService } from './movement-entry-form.service';

describe('MovementEntry Form Service', () => {
  let service: MovementEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovementEntryFormService);
  });

  describe('Service methods', () => {
    describe('createMovementEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMovementEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            startTime: expect.any(Object),
            startNote: expect.any(Object),
            endDate: expect.any(Object),
            endTime: expect.any(Object),
            endNote: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionBy: expect.any(Object),
          })
        );
      });

      it('passing IMovementEntry should create a new form with FormGroup', () => {
        const formGroup = service.createMovementEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            startTime: expect.any(Object),
            startNote: expect.any(Object),
            endDate: expect.any(Object),
            endTime: expect.any(Object),
            endNote: expect.any(Object),
            type: expect.any(Object),
            status: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            note: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionBy: expect.any(Object),
          })
        );
      });
    });

    describe('getMovementEntry', () => {
      it('should return NewMovementEntry for default MovementEntry initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMovementEntryFormGroup(sampleWithNewData);

        const movementEntry = service.getMovementEntry(formGroup) as any;

        expect(movementEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewMovementEntry for empty MovementEntry initial value', () => {
        const formGroup = service.createMovementEntryFormGroup();

        const movementEntry = service.getMovementEntry(formGroup) as any;

        expect(movementEntry).toMatchObject({});
      });

      it('should return IMovementEntry', () => {
        const formGroup = service.createMovementEntryFormGroup(sampleWithRequiredData);

        const movementEntry = service.getMovementEntry(formGroup) as any;

        expect(movementEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMovementEntry should not enable id FormControl', () => {
        const formGroup = service.createMovementEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMovementEntry should disable id FormControl', () => {
        const formGroup = service.createMovementEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
