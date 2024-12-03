import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../arrear-salary.test-samples';

import { ArrearSalaryFormService } from './arrear-salary-form.service';

describe('ArrearSalary Form Service', () => {
  let service: ArrearSalaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArrearSalaryFormService);
  });

  describe('Service methods', () => {
    describe('createArrearSalaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArrearSalaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            arrearType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IArrearSalary should create a new form with FormGroup', () => {
        const formGroup = service.createArrearSalaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            arrearType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getArrearSalary', () => {
      it('should return NewArrearSalary for default ArrearSalary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createArrearSalaryFormGroup(sampleWithNewData);

        const arrearSalary = service.getArrearSalary(formGroup) as any;

        expect(arrearSalary).toMatchObject(sampleWithNewData);
      });

      it('should return NewArrearSalary for empty ArrearSalary initial value', () => {
        const formGroup = service.createArrearSalaryFormGroup();

        const arrearSalary = service.getArrearSalary(formGroup) as any;

        expect(arrearSalary).toMatchObject({});
      });

      it('should return IArrearSalary', () => {
        const formGroup = service.createArrearSalaryFormGroup(sampleWithRequiredData);

        const arrearSalary = service.getArrearSalary(formGroup) as any;

        expect(arrearSalary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArrearSalary should not enable id FormControl', () => {
        const formGroup = service.createArrearSalaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArrearSalary should disable id FormControl', () => {
        const formGroup = service.createArrearSalaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
