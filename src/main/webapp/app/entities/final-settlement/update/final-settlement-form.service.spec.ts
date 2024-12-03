import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../final-settlement.test-samples';

import { FinalSettlementFormService } from './final-settlement-form.service';

describe('FinalSettlement Form Service', () => {
  let service: FinalSettlementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinalSettlementFormService);
  });

  describe('Service methods', () => {
    describe('createFinalSettlementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinalSettlementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateOfResignation: expect.any(Object),
            noticePeriod: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            dateOfRelease: expect.any(Object),
            serviceTenure: expect.any(Object),
            mBasic: expect.any(Object),
            mHouseRent: expect.any(Object),
            mMedical: expect.any(Object),
            mConveyance: expect.any(Object),
            salaryPayable: expect.any(Object),
            salaryPayableRemarks: expect.any(Object),
            totalDaysForLeaveEncashment: expect.any(Object),
            totalLeaveEncashment: expect.any(Object),
            mobileBillInCash: expect.any(Object),
            allowance01Name: expect.any(Object),
            allowance01Amount: expect.any(Object),
            allowance01Remarks: expect.any(Object),
            allowance02Name: expect.any(Object),
            allowance02Amount: expect.any(Object),
            allowance02Remarks: expect.any(Object),
            allowance03Name: expect.any(Object),
            allowance03Amount: expect.any(Object),
            allowance03Remarks: expect.any(Object),
            allowance04Name: expect.any(Object),
            allowance04Amount: expect.any(Object),
            allowance04Remarks: expect.any(Object),
            deductionNoticePay: expect.any(Object),
            deductionPf: expect.any(Object),
            deductionHaf: expect.any(Object),
            deductionExcessCellBill: expect.any(Object),
            deductionAbsentDaysAdjustment: expect.any(Object),
            totalSalaryPayable: expect.any(Object),
            deductionAnnualIncomeTax: expect.any(Object),
            netSalaryPayable: expect.any(Object),
            totalPayablePf: expect.any(Object),
            totalPayableGf: expect.any(Object),
            totalFinalSettlementAmount: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            deductionNoticePayDays: expect.any(Object),
            deductionAbsentDaysAdjustmentDays: expect.any(Object),
            deductionOther: expect.any(Object),
            totalSalary: expect.any(Object),
            totalGrossSalary: expect.any(Object),
            totalDeduction: expect.any(Object),
            finalSettlementDate: expect.any(Object),
            isFinalized: expect.any(Object),
            salaryNumOfMonth: expect.any(Object),
            remarks: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IFinalSettlement should create a new form with FormGroup', () => {
        const formGroup = service.createFinalSettlementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateOfResignation: expect.any(Object),
            noticePeriod: expect.any(Object),
            lastWorkingDay: expect.any(Object),
            dateOfRelease: expect.any(Object),
            serviceTenure: expect.any(Object),
            mBasic: expect.any(Object),
            mHouseRent: expect.any(Object),
            mMedical: expect.any(Object),
            mConveyance: expect.any(Object),
            salaryPayable: expect.any(Object),
            salaryPayableRemarks: expect.any(Object),
            totalDaysForLeaveEncashment: expect.any(Object),
            totalLeaveEncashment: expect.any(Object),
            mobileBillInCash: expect.any(Object),
            allowance01Name: expect.any(Object),
            allowance01Amount: expect.any(Object),
            allowance01Remarks: expect.any(Object),
            allowance02Name: expect.any(Object),
            allowance02Amount: expect.any(Object),
            allowance02Remarks: expect.any(Object),
            allowance03Name: expect.any(Object),
            allowance03Amount: expect.any(Object),
            allowance03Remarks: expect.any(Object),
            allowance04Name: expect.any(Object),
            allowance04Amount: expect.any(Object),
            allowance04Remarks: expect.any(Object),
            deductionNoticePay: expect.any(Object),
            deductionPf: expect.any(Object),
            deductionHaf: expect.any(Object),
            deductionExcessCellBill: expect.any(Object),
            deductionAbsentDaysAdjustment: expect.any(Object),
            totalSalaryPayable: expect.any(Object),
            deductionAnnualIncomeTax: expect.any(Object),
            netSalaryPayable: expect.any(Object),
            totalPayablePf: expect.any(Object),
            totalPayableGf: expect.any(Object),
            totalFinalSettlementAmount: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            deductionNoticePayDays: expect.any(Object),
            deductionAbsentDaysAdjustmentDays: expect.any(Object),
            deductionOther: expect.any(Object),
            totalSalary: expect.any(Object),
            totalGrossSalary: expect.any(Object),
            totalDeduction: expect.any(Object),
            finalSettlementDate: expect.any(Object),
            isFinalized: expect.any(Object),
            salaryNumOfMonth: expect.any(Object),
            remarks: expect.any(Object),
            employee: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getFinalSettlement', () => {
      it('should return NewFinalSettlement for default FinalSettlement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFinalSettlementFormGroup(sampleWithNewData);

        const finalSettlement = service.getFinalSettlement(formGroup) as any;

        expect(finalSettlement).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinalSettlement for empty FinalSettlement initial value', () => {
        const formGroup = service.createFinalSettlementFormGroup();

        const finalSettlement = service.getFinalSettlement(formGroup) as any;

        expect(finalSettlement).toMatchObject({});
      });

      it('should return IFinalSettlement', () => {
        const formGroup = service.createFinalSettlementFormGroup(sampleWithRequiredData);

        const finalSettlement = service.getFinalSettlement(formGroup) as any;

        expect(finalSettlement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinalSettlement should not enable id FormControl', () => {
        const formGroup = service.createFinalSettlementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinalSettlement should disable id FormControl', () => {
        const formGroup = service.createFinalSettlementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
