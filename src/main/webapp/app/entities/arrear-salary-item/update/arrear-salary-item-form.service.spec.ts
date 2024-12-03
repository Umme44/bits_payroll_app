import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../arrear-salary-item.test-samples';

import { ArrearSalaryItemFormService } from './arrear-salary-item-form.service';

describe('ArrearSalaryItem Form Service', () => {
  let service: ArrearSalaryItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArrearSalaryItemFormService);
  });

  describe('Service methods', () => {
    describe('createArrearSalaryItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArrearSalaryItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            arrearAmount: expect.any(Object),
            hasPfArrearDeduction: expect.any(Object),
            pfArrearDeduction: expect.any(Object),
            isFestivalBonus: expect.any(Object),
            isDeleted: expect.any(Object),
            arrearSalaryMaster: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IArrearSalaryItem should create a new form with FormGroup', () => {
        const formGroup = service.createArrearSalaryItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            arrearAmount: expect.any(Object),
            hasPfArrearDeduction: expect.any(Object),
            pfArrearDeduction: expect.any(Object),
            isFestivalBonus: expect.any(Object),
            isDeleted: expect.any(Object),
            arrearSalaryMaster: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getArrearSalaryItem', () => {
      it('should return NewArrearSalaryItem for default ArrearSalaryItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createArrearSalaryItemFormGroup(sampleWithNewData);

        const arrearSalaryItem = service.getArrearSalaryItem(formGroup) as any;

        expect(arrearSalaryItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewArrearSalaryItem for empty ArrearSalaryItem initial value', () => {
        const formGroup = service.createArrearSalaryItemFormGroup();

        const arrearSalaryItem = service.getArrearSalaryItem(formGroup) as any;

        expect(arrearSalaryItem).toMatchObject({});
      });

      it('should return IArrearSalaryItem', () => {
        const formGroup = service.createArrearSalaryItemFormGroup(sampleWithRequiredData);

        const arrearSalaryItem = service.getArrearSalaryItem(formGroup) as any;

        expect(arrearSalaryItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArrearSalaryItem should not enable id FormControl', () => {
        const formGroup = service.createArrearSalaryItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArrearSalaryItem should disable id FormControl', () => {
        const formGroup = service.createArrearSalaryItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
