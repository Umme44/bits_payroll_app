import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hold-fb-disbursement.test-samples';

import { HoldFbDisbursementFormService } from './hold-fb-disbursement-form.service';

describe('HoldFbDisbursement Form Service', () => {
  let service: HoldFbDisbursementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HoldFbDisbursementFormService);
  });

  describe('Service methods', () => {
    describe('createHoldFbDisbursementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            disbursedAt: expect.any(Object),
            remarks: expect.any(Object),
            disbursedBy: expect.any(Object),
            festivalBonusDetail: expect.any(Object),
          })
        );
      });

      it('passing IHoldFbDisbursement should create a new form with FormGroup', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            disbursedAt: expect.any(Object),
            remarks: expect.any(Object),
            disbursedBy: expect.any(Object),
            festivalBonusDetail: expect.any(Object),
          })
        );
      });
    });

    describe('getHoldFbDisbursement', () => {
      it('should return NewHoldFbDisbursement for default HoldFbDisbursement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHoldFbDisbursementFormGroup(sampleWithNewData);

        const holdFbDisbursement = service.getHoldFbDisbursement(formGroup) as any;

        expect(holdFbDisbursement).toMatchObject(sampleWithNewData);
      });

      it('should return NewHoldFbDisbursement for empty HoldFbDisbursement initial value', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup();

        const holdFbDisbursement = service.getHoldFbDisbursement(formGroup) as any;

        expect(holdFbDisbursement).toMatchObject({});
      });

      it('should return IHoldFbDisbursement', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup(sampleWithRequiredData);

        const holdFbDisbursement = service.getHoldFbDisbursement(formGroup) as any;

        expect(holdFbDisbursement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHoldFbDisbursement should not enable id FormControl', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHoldFbDisbursement should disable id FormControl', () => {
        const formGroup = service.createHoldFbDisbursementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
