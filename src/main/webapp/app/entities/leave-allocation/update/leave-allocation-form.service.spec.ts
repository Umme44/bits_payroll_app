import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../leave-allocation.test-samples';

import { LeaveAllocationFormService } from './leave-allocation-form.service';

describe('LeaveAllocation Form Service', () => {
  let service: LeaveAllocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveAllocationFormService);
  });

  describe('Service methods', () => {
    describe('createLeaveAllocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeaveAllocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            leaveType: expect.any(Object),
            allocatedDays: expect.any(Object),
          })
        );
      });

      it('passing ILeaveAllocation should create a new form with FormGroup', () => {
        const formGroup = service.createLeaveAllocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            leaveType: expect.any(Object),
            allocatedDays: expect.any(Object),
          })
        );
      });
    });

    describe('getLeaveAllocation', () => {
      it('should return NewLeaveAllocation for default LeaveAllocation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLeaveAllocationFormGroup(sampleWithNewData);

        const leaveAllocation = service.getLeaveAllocation(formGroup) as any;

        expect(leaveAllocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewLeaveAllocation for empty LeaveAllocation initial value', () => {
        const formGroup = service.createLeaveAllocationFormGroup();

        const leaveAllocation = service.getLeaveAllocation(formGroup) as any;

        expect(leaveAllocation).toMatchObject({});
      });

      it('should return ILeaveAllocation', () => {
        const formGroup = service.createLeaveAllocationFormGroup(sampleWithRequiredData);

        const leaveAllocation = service.getLeaveAllocation(formGroup) as any;

        expect(leaveAllocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILeaveAllocation should not enable id FormControl', () => {
        const formGroup = service.createLeaveAllocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLeaveAllocation should disable id FormControl', () => {
        const formGroup = service.createLeaveAllocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
