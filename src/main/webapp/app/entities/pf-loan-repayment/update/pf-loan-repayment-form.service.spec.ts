import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-loan-repayment.test-samples';

import { PfLoanRepaymentFormService } from './pf-loan-repayment-form.service';

describe('PfLoanRepayment Form Service', () => {
  let service: PfLoanRepaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfLoanRepaymentFormService);
  });

  describe('Service methods', () => {
    describe('createPfLoanRepaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            status: expect.any(Object),
            deductionMonth: expect.any(Object),
            deductionYear: expect.any(Object),
            deductionDate: expect.any(Object),
            pfLoan: expect.any(Object),
          })
        );
      });

      it('passing IPfLoanRepayment should create a new form with FormGroup', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            status: expect.any(Object),
            deductionMonth: expect.any(Object),
            deductionYear: expect.any(Object),
            deductionDate: expect.any(Object),
            pfLoan: expect.any(Object),
          })
        );
      });
    });

    describe('getPfLoanRepayment', () => {
      it('should return NewPfLoanRepayment for default PfLoanRepayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfLoanRepaymentFormGroup(sampleWithNewData);

        const pfLoanRepayment = service.getPfLoanRepayment(formGroup) as any;

        expect(pfLoanRepayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfLoanRepayment for empty PfLoanRepayment initial value', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup();

        const pfLoanRepayment = service.getPfLoanRepayment(formGroup) as any;

        expect(pfLoanRepayment).toMatchObject({});
      });

      it('should return IPfLoanRepayment', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup(sampleWithRequiredData);

        const pfLoanRepayment = service.getPfLoanRepayment(formGroup) as any;

        expect(pfLoanRepayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfLoanRepayment should not enable id FormControl', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfLoanRepayment should disable id FormControl', () => {
        const formGroup = service.createPfLoanRepaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
