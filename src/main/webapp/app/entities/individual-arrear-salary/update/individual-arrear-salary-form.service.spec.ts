import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../individual-arrear-salary.test-samples';

import { IndividualArrearSalaryFormService } from './individual-arrear-salary-form.service';

describe('IndividualArrearSalary Form Service', () => {
  let service: IndividualArrearSalaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IndividualArrearSalaryFormService);
  });

  describe('Service methods', () => {
    describe('createIndividualArrearSalaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveDate: expect.any(Object),
            existingBand: expect.any(Object),
            newBand: expect.any(Object),
            existingGross: expect.any(Object),
            newGross: expect.any(Object),
            increment: expect.any(Object),
            arrearSalary: expect.any(Object),
            arrearPfDeduction: expect.any(Object),
            taxDeduction: expect.any(Object),
            netPay: expect.any(Object),
            pfContribution: expect.any(Object),
            title: expect.any(Object),
            titleEffectiveFrom: expect.any(Object),
            arrearRemarks: expect.any(Object),
            festivalBonus: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IIndividualArrearSalary should create a new form with FormGroup', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            effectiveDate: expect.any(Object),
            existingBand: expect.any(Object),
            newBand: expect.any(Object),
            existingGross: expect.any(Object),
            newGross: expect.any(Object),
            increment: expect.any(Object),
            arrearSalary: expect.any(Object),
            arrearPfDeduction: expect.any(Object),
            taxDeduction: expect.any(Object),
            netPay: expect.any(Object),
            pfContribution: expect.any(Object),
            title: expect.any(Object),
            titleEffectiveFrom: expect.any(Object),
            arrearRemarks: expect.any(Object),
            festivalBonus: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getIndividualArrearSalary', () => {
      it('should return NewIndividualArrearSalary for default IndividualArrearSalary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIndividualArrearSalaryFormGroup(sampleWithNewData);

        const individualArrearSalary = service.getIndividualArrearSalary(formGroup) as any;

        expect(individualArrearSalary).toMatchObject(sampleWithNewData);
      });

      it('should return NewIndividualArrearSalary for empty IndividualArrearSalary initial value', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup();

        const individualArrearSalary = service.getIndividualArrearSalary(formGroup) as any;

        expect(individualArrearSalary).toMatchObject({});
      });

      it('should return IIndividualArrearSalary', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup(sampleWithRequiredData);

        const individualArrearSalary = service.getIndividualArrearSalary(formGroup) as any;

        expect(individualArrearSalary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIndividualArrearSalary should not enable id FormControl', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIndividualArrearSalary should disable id FormControl', () => {
        const formGroup = service.createIndividualArrearSalaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
