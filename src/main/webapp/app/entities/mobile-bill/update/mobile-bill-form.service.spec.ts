import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../mobile-bill.test-samples';

import { MobileBillFormService } from './mobile-bill-form.service';

describe('MobileBill Form Service', () => {
  let service: MobileBillFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MobileBillFormService);
  });

  describe('Service methods', () => {
    describe('createMobileBillFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMobileBillFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            amount: expect.any(Object),
            year: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IMobileBill should create a new form with FormGroup', () => {
        const formGroup = service.createMobileBillFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            amount: expect.any(Object),
            year: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getMobileBill', () => {
      it('should return NewMobileBill for default MobileBill initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMobileBillFormGroup(sampleWithNewData);

        const mobileBill = service.getMobileBill(formGroup) as any;

        expect(mobileBill).toMatchObject(sampleWithNewData);
      });

      it('should return NewMobileBill for empty MobileBill initial value', () => {
        const formGroup = service.createMobileBillFormGroup();

        const mobileBill = service.getMobileBill(formGroup) as any;

        expect(mobileBill).toMatchObject({});
      });

      it('should return IMobileBill', () => {
        const formGroup = service.createMobileBillFormGroup(sampleWithRequiredData);

        const mobileBill = service.getMobileBill(formGroup) as any;

        expect(mobileBill).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMobileBill should not enable id FormControl', () => {
        const formGroup = service.createMobileBillFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMobileBill should disable id FormControl', () => {
        const formGroup = service.createMobileBillFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
