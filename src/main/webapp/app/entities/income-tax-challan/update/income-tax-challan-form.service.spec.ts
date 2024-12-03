import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../income-tax-challan.test-samples';

import { IncomeTaxChallanFormService } from './income-tax-challan-form.service';

describe('IncomeTaxChallan Form Service', () => {
  let service: IncomeTaxChallanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncomeTaxChallanFormService);
  });

  describe('Service methods', () => {
    describe('createIncomeTaxChallanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            challanNo: expect.any(Object),
            challanDate: expect.any(Object),
            amount: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            remarks: expect.any(Object),
            aitConfig: expect.any(Object),
          })
        );
      });

      it('passing IIncomeTaxChallan should create a new form with FormGroup', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            challanNo: expect.any(Object),
            challanDate: expect.any(Object),
            amount: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            remarks: expect.any(Object),
            aitConfig: expect.any(Object),
          })
        );
      });
    });

    describe('getIncomeTaxChallan', () => {
      it('should return NewIncomeTaxChallan for default IncomeTaxChallan initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIncomeTaxChallanFormGroup(sampleWithNewData);

        const incomeTaxChallan = service.getIncomeTaxChallan(formGroup) as any;

        expect(incomeTaxChallan).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncomeTaxChallan for empty IncomeTaxChallan initial value', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup();

        const incomeTaxChallan = service.getIncomeTaxChallan(formGroup) as any;

        expect(incomeTaxChallan).toMatchObject({});
      });

      it('should return IIncomeTaxChallan', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup(sampleWithRequiredData);

        const incomeTaxChallan = service.getIncomeTaxChallan(formGroup) as any;

        expect(incomeTaxChallan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncomeTaxChallan should not enable id FormControl', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncomeTaxChallan should disable id FormControl', () => {
        const formGroup = service.createIncomeTaxChallanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
