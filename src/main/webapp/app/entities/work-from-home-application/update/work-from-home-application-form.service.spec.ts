import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../work-from-home-application.test-samples';

import { WorkFromHomeApplicationFormService } from './work-from-home-application-form.service';

describe('WorkFromHomeApplication Form Service', () => {
  let service: WorkFromHomeApplicationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkFromHomeApplicationFormService);
  });

  describe('Service methods', () => {
    describe('createWorkFromHomeApplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            reason: expect.any(Object),
            duration: expect.any(Object),
            status: expect.any(Object),
            appliedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdAt: expect.any(Object),
            sanctionedAt: expect.any(Object),
            appliedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionedBy: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IWorkFromHomeApplication should create a new form with FormGroup', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            reason: expect.any(Object),
            duration: expect.any(Object),
            status: expect.any(Object),
            appliedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdAt: expect.any(Object),
            sanctionedAt: expect.any(Object),
            appliedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionedBy: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getWorkFromHomeApplication', () => {
      it('should return NewWorkFromHomeApplication for default WorkFromHomeApplication initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWorkFromHomeApplicationFormGroup(sampleWithNewData);

        const workFromHomeApplication = service.getWorkFromHomeApplication(formGroup) as any;

        expect(workFromHomeApplication).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkFromHomeApplication for empty WorkFromHomeApplication initial value', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup();

        const workFromHomeApplication = service.getWorkFromHomeApplication(formGroup) as any;

        expect(workFromHomeApplication).toMatchObject({});
      });

      it('should return IWorkFromHomeApplication', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup(sampleWithRequiredData);

        const workFromHomeApplication = service.getWorkFromHomeApplication(formGroup) as any;

        expect(workFromHomeApplication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkFromHomeApplication should not enable id FormControl', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkFromHomeApplication should disable id FormControl', () => {
        const formGroup = service.createWorkFromHomeApplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
