import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../leave-balance.test-samples';

import { LeaveBalanceFormService } from './leave-balance-form.service';

describe('LeaveBalance Form Service', () => {
  let service: LeaveBalanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveBalanceFormService);
  });

  describe('Service methods', () => {
    describe('createLeaveBalanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeaveBalanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            leaveType: expect.any(Object),
            openingBalance: expect.any(Object),
            closingBalance: expect.any(Object),
            consumedDuringYear: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            leaveAmountType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing ILeaveBalance should create a new form with FormGroup', () => {
        const formGroup = service.createLeaveBalanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            leaveType: expect.any(Object),
            openingBalance: expect.any(Object),
            closingBalance: expect.any(Object),
            consumedDuringYear: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            leaveAmountType: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getLeaveBalance', () => {
      it('should return NewLeaveBalance for default LeaveBalance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLeaveBalanceFormGroup(sampleWithNewData);

        const leaveBalance = service.getLeaveBalance(formGroup) as any;

        expect(leaveBalance).toMatchObject(sampleWithNewData);
      });

      it('should return NewLeaveBalance for empty LeaveBalance initial value', () => {
        const formGroup = service.createLeaveBalanceFormGroup();

        const leaveBalance = service.getLeaveBalance(formGroup) as any;

        expect(leaveBalance).toMatchObject({});
      });

      it('should return ILeaveBalance', () => {
        const formGroup = service.createLeaveBalanceFormGroup(sampleWithRequiredData);

        const leaveBalance = service.getLeaveBalance(formGroup) as any;

        expect(leaveBalance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILeaveBalance should not enable id FormControl', () => {
        const formGroup = service.createLeaveBalanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLeaveBalance should disable id FormControl', () => {
        const formGroup = service.createLeaveBalanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
