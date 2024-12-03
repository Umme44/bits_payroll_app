import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-feedback.test-samples';

import { UserFeedbackFormService } from './user-feedback-form.service';

describe('UserFeedback Form Service', () => {
  let service: UserFeedbackFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserFeedbackFormService);
  });

  describe('Service methods', () => {
    describe('createUserFeedbackFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserFeedbackFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            suggestion: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IUserFeedback should create a new form with FormGroup', () => {
        const formGroup = service.createUserFeedbackFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            suggestion: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getUserFeedback', () => {
      it('should return NewUserFeedback for default UserFeedback initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserFeedbackFormGroup(sampleWithNewData);

        const userFeedback = service.getUserFeedback(formGroup) as any;

        expect(userFeedback).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserFeedback for empty UserFeedback initial value', () => {
        const formGroup = service.createUserFeedbackFormGroup();

        const userFeedback = service.getUserFeedback(formGroup) as any;

        expect(userFeedback).toMatchObject({});
      });

      it('should return IUserFeedback', () => {
        const formGroup = service.createUserFeedbackFormGroup(sampleWithRequiredData);

        const userFeedback = service.getUserFeedback(formGroup) as any;

        expect(userFeedback).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserFeedback should not enable id FormControl', () => {
        const formGroup = service.createUserFeedbackFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserFeedback should disable id FormControl', () => {
        const formGroup = service.createUserFeedbackFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
