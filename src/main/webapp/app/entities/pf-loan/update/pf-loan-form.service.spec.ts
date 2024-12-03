import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-loan.test-samples';

import { PfLoanFormService } from './pf-loan-form.service';

describe('PfLoan Form Service', () => {
  let service: PfLoanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfLoanFormService);
  });

  describe('Service methods', () => {
    describe('createPfLoanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfLoanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            disbursementAmount: expect.any(Object),
            disbursementDate: expect.any(Object),
            bankName: expect.any(Object),
            bankBranch: expect.any(Object),
            bankAccountNumber: expect.any(Object),
            chequeNumber: expect.any(Object),
            instalmentNumber: expect.any(Object),
            installmentAmount: expect.any(Object),
            instalmentStartFrom: expect.any(Object),
            status: expect.any(Object),
            pfLoanApplication: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });

      it('passing IPfLoan should create a new form with FormGroup', () => {
        const formGroup = service.createPfLoanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            disbursementAmount: expect.any(Object),
            disbursementDate: expect.any(Object),
            bankName: expect.any(Object),
            bankBranch: expect.any(Object),
            bankAccountNumber: expect.any(Object),
            chequeNumber: expect.any(Object),
            instalmentNumber: expect.any(Object),
            installmentAmount: expect.any(Object),
            instalmentStartFrom: expect.any(Object),
            status: expect.any(Object),
            pfLoanApplication: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });
    });

    describe('getPfLoan', () => {
      it('should return NewPfLoan for default PfLoan initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfLoanFormGroup(sampleWithNewData);

        const pfLoan = service.getPfLoan(formGroup) as any;

        expect(pfLoan).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfLoan for empty PfLoan initial value', () => {
        const formGroup = service.createPfLoanFormGroup();

        const pfLoan = service.getPfLoan(formGroup) as any;

        expect(pfLoan).toMatchObject({});
      });

      it('should return IPfLoan', () => {
        const formGroup = service.createPfLoanFormGroup(sampleWithRequiredData);

        const pfLoan = service.getPfLoan(formGroup) as any;

        expect(pfLoan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfLoan should not enable id FormControl', () => {
        const formGroup = service.createPfLoanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfLoan should disable id FormControl', () => {
        const formGroup = service.createPfLoanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
