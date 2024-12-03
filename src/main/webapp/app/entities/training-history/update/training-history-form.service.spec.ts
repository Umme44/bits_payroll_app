import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../training-history.test-samples';

import { TrainingHistoryFormService } from './training-history-form.service';

describe('TrainingHistory Form Service', () => {
  let service: TrainingHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrainingHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createTrainingHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrainingHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainingName: expect.any(Object),
            coordinatedBy: expect.any(Object),
            dateOfCompletion: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing ITrainingHistory should create a new form with FormGroup', () => {
        const formGroup = service.createTrainingHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainingName: expect.any(Object),
            coordinatedBy: expect.any(Object),
            dateOfCompletion: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getTrainingHistory', () => {
      it('should return NewTrainingHistory for default TrainingHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrainingHistoryFormGroup(sampleWithNewData);

        const trainingHistory = service.getTrainingHistory(formGroup) as any;

        expect(trainingHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrainingHistory for empty TrainingHistory initial value', () => {
        const formGroup = service.createTrainingHistoryFormGroup();

        const trainingHistory = service.getTrainingHistory(formGroup) as any;

        expect(trainingHistory).toMatchObject({});
      });

      it('should return ITrainingHistory', () => {
        const formGroup = service.createTrainingHistoryFormGroup(sampleWithRequiredData);

        const trainingHistory = service.getTrainingHistory(formGroup) as any;

        expect(trainingHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrainingHistory should not enable id FormControl', () => {
        const formGroup = service.createTrainingHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrainingHistory should disable id FormControl', () => {
        const formGroup = service.createTrainingHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
