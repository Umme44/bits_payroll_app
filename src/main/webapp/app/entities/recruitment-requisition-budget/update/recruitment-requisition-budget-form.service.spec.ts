import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../recruitment-requisition-budget.test-samples';

import { RecruitmentRequisitionBudgetFormService } from './recruitment-requisition-budget-form.service';

describe('RecruitmentRequisitionBudget Form Service', () => {
  let service: RecruitmentRequisitionBudgetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecruitmentRequisitionBudgetFormService);
  });

  describe('Service methods', () => {
    describe('createRecruitmentRequisitionBudgetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            budget: expect.any(Object),
            remainingBudget: expect.any(Object),
            remainingManpower: expect.any(Object),
            employee: expect.any(Object),
            department: expect.any(Object),
          })
        );
      });

      it('passing IRecruitmentRequisitionBudget should create a new form with FormGroup', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            budget: expect.any(Object),
            remainingBudget: expect.any(Object),
            remainingManpower: expect.any(Object),
            employee: expect.any(Object),
            department: expect.any(Object),
          })
        );
      });
    });

    describe('getRecruitmentRequisitionBudget', () => {
      it('should return NewRecruitmentRequisitionBudget for default RecruitmentRequisitionBudget initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup(sampleWithNewData);

        const recruitmentRequisitionBudget = service.getRecruitmentRequisitionBudget(formGroup) as any;

        expect(recruitmentRequisitionBudget).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecruitmentRequisitionBudget for empty RecruitmentRequisitionBudget initial value', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup();

        const recruitmentRequisitionBudget = service.getRecruitmentRequisitionBudget(formGroup) as any;

        expect(recruitmentRequisitionBudget).toMatchObject({});
      });

      it('should return IRecruitmentRequisitionBudget', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup(sampleWithRequiredData);

        const recruitmentRequisitionBudget = service.getRecruitmentRequisitionBudget(formGroup) as any;

        expect(recruitmentRequisitionBudget).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecruitmentRequisitionBudget should not enable id FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecruitmentRequisitionBudget should disable id FormControl', () => {
        const formGroup = service.createRecruitmentRequisitionBudgetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
