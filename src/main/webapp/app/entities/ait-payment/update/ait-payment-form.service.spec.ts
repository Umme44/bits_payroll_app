import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ait-payment.test-samples';

import { AitPaymentFormService } from './ait-payment-form.service';

describe('AitPayment Form Service', () => {
  let service: AitPaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AitPaymentFormService);
  });

  describe('Service methods', () => {
    describe('createAitPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAitPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IAitPayment should create a new form with FormGroup', () => {
        const formGroup = service.createAitPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getAitPayment', () => {
      it('should return NewAitPayment for default AitPayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAitPaymentFormGroup(sampleWithNewData);

        const aitPayment = service.getAitPayment(formGroup) as any;

        expect(aitPayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewAitPayment for empty AitPayment initial value', () => {
        const formGroup = service.createAitPaymentFormGroup();

        const aitPayment = service.getAitPayment(formGroup) as any;

        expect(aitPayment).toMatchObject({});
      });

      it('should return IAitPayment', () => {
        const formGroup = service.createAitPaymentFormGroup(sampleWithRequiredData);

        const aitPayment = service.getAitPayment(formGroup) as any;

        expect(aitPayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAitPayment should not enable id FormControl', () => {
        const formGroup = service.createAitPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAitPayment should disable id FormControl', () => {
        const formGroup = service.createAitPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
