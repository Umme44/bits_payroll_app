import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../recruitment-requisition-form.test-samples';

import { RecruitmentRequisitionFormFormService } from './recruitment-requisition-form-form.service';

describe('RecruitmentRequisitionForm Form Service', () => {
  let service: RecruitmentRequisitionFormFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecruitmentRequisitionFormFormService);
  });

  describe('Service methods', () => {
    describe('createRecruitmentRequisitionFormFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            expectedJoiningDate: expect.any(Object),
            project: expect.any(Object),
            numberOfVacancies: expect.any(Object),
            employmentType: expect.any(Object),
            resourceType: expect.any(Object),
            rrfNumber: expect.any(Object),
            preferredEducationType: expect.any(Object),
            dateOfRequisition: expect.any(Object),
            requestedDate: expect.any(Object),
            recommendationDate01: expect.any(Object),
            recommendationDate02: expect.any(Object),
            recommendationDate03: expect.any(Object),
            recommendationDate04: expect.any(Object),
            requisitionStatus: expect.any(Object),
            rejectedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            isDeleted: expect.any(Object),
            totalOnboard: expect.any(Object),
            preferredSkillType: expect.any(Object),
            recruitmentNature: expect.any(Object),
            specialRequirement: expect.any(Object),
            recommendationDate05: expect.any(Object),
            functionalDesignation: expect.any(Object),
            band: expect.any(Object),
            department: expect.any(Object),
            unit: expect.any(Object),
            recommendedBy01: expect.any(Object),
            recommendedBy02: expect.any(Object),
            recommendedBy03: expect.any(Object),
            recommendedBy04: expect.any(Object),
            requester: expect.any(Object),
            rejectedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            deletedBy: expect.any(Object),
            recommendedBy05: expect.any(Object),
            employeeToBeReplaced: expect.any(Object),
          })
        );
      });

      it('passing IRecruitmentRequisitionForm should create a new form with FormGroup', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            expectedJoiningDate: expect.any(Object),
            project: expect.any(Object),
            numberOfVacancies: expect.any(Object),
            employmentType: expect.any(Object),
            resourceType: expect.any(Object),
            rrfNumber: expect.any(Object),
            preferredEducationType: expect.any(Object),
            dateOfRequisition: expect.any(Object),
            requestedDate: expect.any(Object),
            recommendationDate01: expect.any(Object),
            recommendationDate02: expect.any(Object),
            recommendationDate03: expect.any(Object),
            recommendationDate04: expect.any(Object),
            requisitionStatus: expect.any(Object),
            rejectedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            isDeleted: expect.any(Object),
            totalOnboard: expect.any(Object),
            preferredSkillType: expect.any(Object),
            recruitmentNature: expect.any(Object),
            specialRequirement: expect.any(Object),
            recommendationDate05: expect.any(Object),
            functionalDesignation: expect.any(Object),
            band: expect.any(Object),
            department: expect.any(Object),
            unit: expect.any(Object),
            recommendedBy01: expect.any(Object),
            recommendedBy02: expect.any(Object),
            recommendedBy03: expect.any(Object),
            recommendedBy04: expect.any(Object),
            requester: expect.any(Object),
            rejectedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            deletedBy: expect.any(Object),
            recommendedBy05: expect.any(Object),
            employeeToBeReplaced: expect.any(Object),
          })
        );
      });
    });

    describe('getRecruitmentRequisitionForm', () => {
      it('should return NewRecruitmentRequisitionForm for default RecruitmentRequisitionForm initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRecruitmentRequisitionFormFormGroup(sampleWithNewData);

        const recruitmentRequisitionForm = service.getRecruitmentRequisitionForm(formGroup) as any;

        expect(recruitmentRequisitionForm).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecruitmentRequisitionForm for empty RecruitmentRequisitionForm initial value', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup();

        const recruitmentRequisitionForm = service.getRecruitmentRequisitionForm(formGroup) as any;

        expect(recruitmentRequisitionForm).toMatchObject({});
      });

      it('should return IRecruitmentRequisitionForm', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup(sampleWithRequiredData);

        const recruitmentRequisitionForm = service.getRecruitmentRequisitionForm(formGroup) as any;

        expect(recruitmentRequisitionForm).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecruitmentRequisitionForm should not enable id FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecruitmentRequisitionForm should disable id FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionFormFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
