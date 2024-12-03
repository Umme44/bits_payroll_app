import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../arrear-payment.test-samples';

import { ArrearPaymentFormService } from './arrear-payment-form.service';

describe('ArrearPayment Form Service', () => {
  let service: ArrearPaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArrearPaymentFormService);
  });

  describe('Service methods', () => {
    describe('createArrearPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArrearPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentType: expect.any(Object),
            disbursementDate: expect.any(Object),
            salaryMonth: expect.any(Object),
            salaryYear: expect.any(Object),
            approvalStatus: expect.any(Object),
            disbursementAmount: expect.any(Object),
            isDeleted: expect.any(Object),
            arrearPF: expect.any(Object),
            taxDeduction: expect.any(Object),
            deductTaxUponPayment: expect.any(Object),
            arrearSalaryItem: expect.any(Object),
          })
        );
      });

      it('passing IArrearPayment should create a new form with FormGroup', () => {
        const formGroup = service.createArrearPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentType: expect.any(Object),
            disbursementDate: expect.any(Object),
            salaryMonth: expect.any(Object),
            salaryYear: expect.any(Object),
            approvalStatus: expect.any(Object),
            disbursementAmount: expect.any(Object),
            isDeleted: expect.any(Object),
            arrearPF: expect.any(Object),
            taxDeduction: expect.any(Object),
            deductTaxUponPayment: expect.any(Object),
            arrearSalaryItem: expect.any(Object),
          })
        );
      });
    });

    describe('getArrearPayment', () => {
      it('should return NewArrearPayment for default ArrearPayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createArrearPaymentFormGroup(sampleWithNewData);

        const arrearPayment = service.getArrearPayment(formGroup) as any;

        expect(arrearPayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewArrearPayment for empty ArrearPayment initial value', () => {
        const formGroup = service.createArrearPaymentFormGroup();

        const arrearPayment = service.getArrearPayment(formGroup) as any;

        expect(arrearPayment).toMatchObject({});
      });

      it('should return IArrearPayment', () => {
        const formGroup = service.createArrearPaymentFormGroup(sampleWithRequiredData);

        const arrearPayment = service.getArrearPayment(formGroup) as any;

        expect(arrearPayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArrearPayment should not enable id FormControl', () => {
        const formGroup = service.createArrearPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArrearPayment should disable id FormControl', () => {
        const formGroup = service.createArrearPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
