import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tax-acknowledgement-receipt.test-samples';

import { TaxAcknowledgementReceiptFormService } from './tax-acknowledgement-receipt-form.service';

describe('TaxAcknowledgementReceipt Form Service', () => {
  let service: TaxAcknowledgementReceiptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaxAcknowledgementReceiptFormService);
  });

  describe('Service methods', () => {
    describe('createTaxAcknowledgementReceiptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tinNumber: expect.any(Object),
            receiptNumber: expect.any(Object),
            taxesCircle: expect.any(Object),
            taxesZone: expect.any(Object),
            dateOfSubmission: expect.any(Object),
            filePath: expect.any(Object),
            acknowledgementStatus: expect.any(Object),
            receivedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            fiscalYear: expect.any(Object),
            employee: expect.any(Object),
            receivedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing ITaxAcknowledgementReceipt should create a new form with FormGroup', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tinNumber: expect.any(Object),
            receiptNumber: expect.any(Object),
            taxesCircle: expect.any(Object),
            taxesZone: expect.any(Object),
            dateOfSubmission: expect.any(Object),
            filePath: expect.any(Object),
            acknowledgementStatus: expect.any(Object),
            receivedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            fiscalYear: expect.any(Object),
            employee: expect.any(Object),
            receivedBy: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getTaxAcknowledgementReceipt', () => {
      it('should return NewTaxAcknowledgementReceipt for default TaxAcknowledgementReceipt initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup(sampleWithNewData);

        const taxAcknowledgementReceipt = service.getTaxAcknowledgementReceipt(formGroup) as any;

        expect(taxAcknowledgementReceipt).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxAcknowledgementReceipt for empty TaxAcknowledgementReceipt initial value', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup();

        const taxAcknowledgementReceipt = service.getTaxAcknowledgementReceipt(formGroup) as any;

        expect(taxAcknowledgementReceipt).toMatchObject({});
      });

      it('should return ITaxAcknowledgementReceipt', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup(sampleWithRequiredData);

        const taxAcknowledgementReceipt = service.getTaxAcknowledgementReceipt(formGroup) as any;

        expect(taxAcknowledgementReceipt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxAcknowledgementReceipt should not enable id FormControl', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxAcknowledgementReceipt should disable id FormControl', () => {
        const formGroup = service.createTaxAcknowledgementReceiptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
