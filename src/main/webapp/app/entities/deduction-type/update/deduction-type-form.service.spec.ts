import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../deduction-type.test-samples';

import { DeductionTypeFormService } from './deduction-type-form.service';

describe('DeductionType Form Service', () => {
  let service: DeductionTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeductionTypeFormService);
  });

  describe('Service methods', () => {
    describe('createDeductionTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeductionTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IDeductionType should create a new form with FormGroup', () => {
        const formGroup = service.createDeductionTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getDeductionType', () => {
      it('should return NewDeductionType for default DeductionType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeductionTypeFormGroup(sampleWithNewData);

        const deductionType = service.getDeductionType(formGroup) as any;

        expect(deductionType).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeductionType for empty DeductionType initial value', () => {
        const formGroup = service.createDeductionTypeFormGroup();

        const deductionType = service.getDeductionType(formGroup) as any;

        expect(deductionType).toMatchObject({});
      });

      it('should return IDeductionType', () => {
        const formGroup = service.createDeductionTypeFormGroup(sampleWithRequiredData);

        const deductionType = service.getDeductionType(formGroup) as any;

        expect(deductionType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeductionType should not enable id FormControl', () => {
        const formGroup = service.createDeductionTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeductionType should disable id FormControl', () => {
        const formGroup = service.createDeductionTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
