import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../working-experience.test-samples';

import { WorkingExperienceFormService } from './working-experience-form.service';

describe('WorkingExperience Form Service', () => {
  let service: WorkingExperienceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkingExperienceFormService);
  });

  describe('Service methods', () => {
    describe('createWorkingExperienceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkingExperienceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastDesignation: expect.any(Object),
            organizationName: expect.any(Object),
            dojOfLastOrganization: expect.any(Object),
            dorOfLastOrganization: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IWorkingExperience should create a new form with FormGroup', () => {
        const formGroup = service.createWorkingExperienceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastDesignation: expect.any(Object),
            organizationName: expect.any(Object),
            dojOfLastOrganization: expect.any(Object),
            dorOfLastOrganization: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getWorkingExperience', () => {
      it('should return NewWorkingExperience for default WorkingExperience initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWorkingExperienceFormGroup(sampleWithNewData);

        const workingExperience = service.getWorkingExperience(formGroup) as any;

        expect(workingExperience).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkingExperience for empty WorkingExperience initial value', () => {
        const formGroup = service.createWorkingExperienceFormGroup();

        const workingExperience = service.getWorkingExperience(formGroup) as any;

        expect(workingExperience).toMatchObject({});
      });

      it('should return IWorkingExperience', () => {
        const formGroup = service.createWorkingExperienceFormGroup(sampleWithRequiredData);

        const workingExperience = service.getWorkingExperience(formGroup) as any;

        expect(workingExperience).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkingExperience should not enable id FormControl', () => {
        const formGroup = service.createWorkingExperienceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkingExperience should disable id FormControl', () => {
        const formGroup = service.createWorkingExperienceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
