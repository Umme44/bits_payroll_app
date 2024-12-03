import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-loan-application.test-samples';

import { PfLoanApplicationFormService } from './pf-loan-application-form.service';

describe('PfLoanApplication Form Service', () => {
  let service: PfLoanApplicationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfLoanApplicationFormService);
  });

  describe('Service methods', () => {
    describe('createPfLoanApplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfLoanApplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            installmentAmount: expect.any(Object),
            noOfInstallment: expect.any(Object),
            remarks: expect.any(Object),
            isRecommended: expect.any(Object),
            recommendationDate: expect.any(Object),
            isApproved: expect.any(Object),
            approvalDate: expect.any(Object),
            isRejected: expect.any(Object),
            rejectionReason: expect.any(Object),
            rejectionDate: expect.any(Object),
            disbursementDate: expect.any(Object),
            disbursementAmount: expect.any(Object),
            status: expect.any(Object),
            recommendedBy: expect.any(Object),
            approvedBy: expect.any(Object),
            rejectedBy: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });

      it('passing IPfLoanApplication should create a new form with FormGroup', () => {
        const formGroup = service.createPfLoanApplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            installmentAmount: expect.any(Object),
            noOfInstallment: expect.any(Object),
            remarks: expect.any(Object),
            isRecommended: expect.any(Object),
            recommendationDate: expect.any(Object),
            isApproved: expect.any(Object),
            approvalDate: expect.any(Object),
            isRejected: expect.any(Object),
            rejectionReason: expect.any(Object),
            rejectionDate: expect.any(Object),
            disbursementDate: expect.any(Object),
            disbursementAmount: expect.any(Object),
            status: expect.any(Object),
            recommendedBy: expect.any(Object),
            approvedBy: expect.any(Object),
            rejectedBy: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });
    });

    describe('getPfLoanApplication', () => {
      it('should return NewPfLoanApplication for default PfLoanApplication initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfLoanApplicationFormGroup(sampleWithNewData);

        const pfLoanApplication = service.getPfLoanApplication(formGroup) as any;

        expect(pfLoanApplication).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfLoanApplication for empty PfLoanApplication initial value', () => {
        const formGroup = service.createPfLoanApplicationFormGroup();

        const pfLoanApplication = service.getPfLoanApplication(formGroup) as any;

        expect(pfLoanApplication).toMatchObject({});
      });

      it('should return IPfLoanApplication', () => {
        const formGroup = service.createPfLoanApplicationFormGroup(sampleWithRequiredData);

        const pfLoanApplication = service.getPfLoanApplication(formGroup) as any;

        expect(pfLoanApplication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfLoanApplication should not enable id FormControl', () => {
        const formGroup = service.createPfLoanApplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfLoanApplication should disable id FormControl', () => {
        const formGroup = service.createPfLoanApplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
