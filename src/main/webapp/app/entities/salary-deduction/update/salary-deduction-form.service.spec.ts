import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../salary-deduction.test-samples';

import { SalaryDeductionFormService } from './salary-deduction-form.service';

describe('SalaryDeduction Form Service', () => {
  let service: SalaryDeductionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryDeductionFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryDeductionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryDeductionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deductionAmount: expect.any(Object),
            deductionYear: expect.any(Object),
            deductionMonth: expect.any(Object),
            deductionType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing ISalaryDeduction should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryDeductionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deductionAmount: expect.any(Object),
            deductionYear: expect.any(Object),
            deductionMonth: expect.any(Object),
            deductionType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getSalaryDeduction', () => {
      it('should return NewSalaryDeduction for default SalaryDeduction initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSalaryDeductionFormGroup(sampleWithNewData);

        const salaryDeduction = service.getSalaryDeduction(formGroup) as any;

        expect(salaryDeduction).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalaryDeduction for empty SalaryDeduction initial value', () => {
        const formGroup = service.createSalaryDeductionFormGroup();

        const salaryDeduction = service.getSalaryDeduction(formGroup) as any;

        expect(salaryDeduction).toMatchObject({});
      });

      it('should return ISalaryDeduction', () => {
        const formGroup = service.createSalaryDeductionFormGroup(sampleWithRequiredData);

        const salaryDeduction = service.getSalaryDeduction(formGroup) as any;

        expect(salaryDeduction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalaryDeduction should not enable id FormControl', () => {
        const formGroup = service.createSalaryDeductionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalaryDeduction should disable id FormControl', () => {
        const formGroup = service.createSalaryDeductionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
