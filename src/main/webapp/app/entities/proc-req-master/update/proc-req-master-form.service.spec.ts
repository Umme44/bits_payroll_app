import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../proc-req-master.test-samples';

import { ProcReqMasterFormService } from './proc-req-master-form.service';

describe('ProcReqMaster Form Service', () => {
  let service: ProcReqMasterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcReqMasterFormService);
  });

  describe('Service methods', () => {
    describe('createProcReqMasterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProcReqMasterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requisitionNo: expect.any(Object),
            requestedDate: expect.any(Object),
            isCTOApprovalRequired: expect.any(Object),
            requisitionStatus: expect.any(Object),
            expectedReceivedDate: expect.any(Object),
            reasoning: expect.any(Object),
            totalApproximatePrice: expect.any(Object),
            recommendationAt01: expect.any(Object),
            recommendationAt02: expect.any(Object),
            recommendationAt03: expect.any(Object),
            recommendationAt04: expect.any(Object),
            recommendationAt05: expect.any(Object),
            nextRecommendationOrder: expect.any(Object),
            rejectedDate: expect.any(Object),
            rejectionReason: expect.any(Object),
            closedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            requestedBy: expect.any(Object),
            recommendedBy01: expect.any(Object),
            recommendedBy02: expect.any(Object),
            recommendedBy03: expect.any(Object),
            recommendedBy04: expect.any(Object),
            recommendedBy05: expect.any(Object),
            nextApprovalFrom: expect.any(Object),
            rejectedBy: expect.any(Object),
            closedBy: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });

      it('passing IProcReqMaster should create a new form with FormGroup', () => {
        const formGroup = service.createProcReqMasterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requisitionNo: expect.any(Object),
            requestedDate: expect.any(Object),
            isCTOApprovalRequired: expect.any(Object),
            requisitionStatus: expect.any(Object),
            expectedReceivedDate: expect.any(Object),
            reasoning: expect.any(Object),
            totalApproximatePrice: expect.any(Object),
            recommendationAt01: expect.any(Object),
            recommendationAt02: expect.any(Object),
            recommendationAt03: expect.any(Object),
            recommendationAt04: expect.any(Object),
            recommendationAt05: expect.any(Object),
            nextRecommendationOrder: expect.any(Object),
            rejectedDate: expect.any(Object),
            rejectionReason: expect.any(Object),
            closedAt: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            requestedBy: expect.any(Object),
            recommendedBy01: expect.any(Object),
            recommendedBy02: expect.any(Object),
            recommendedBy03: expect.any(Object),
            recommendedBy04: expect.any(Object),
            recommendedBy05: expect.any(Object),
            nextApprovalFrom: expect.any(Object),
            rejectedBy: expect.any(Object),
            closedBy: expect.any(Object),
            updatedBy: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });
    });

    describe('getProcReqMaster', () => {
      it('should return NewProcReqMaster for default ProcReqMaster initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProcReqMasterFormGroup(sampleWithNewData);

        const procReqMaster = service.getProcReqMaster(formGroup) as any;

        expect(procReqMaster).toMatchObject(sampleWithNewData);
      });

      it('should return NewProcReqMaster for empty ProcReqMaster initial value', () => {
        const formGroup = service.createProcReqMasterFormGroup();

        const procReqMaster = service.getProcReqMaster(formGroup) as any;

        expect(procReqMaster).toMatchObject({});
      });

      it('should return IProcReqMaster', () => {
        const formGroup = service.createProcReqMasterFormGroup(sampleWithRequiredData);

        const procReqMaster = service.getProcReqMaster(formGroup) as any;

        expect(procReqMaster).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProcReqMaster should not enable id FormControl', () => {
        const formGroup = service.createProcReqMasterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProcReqMaster should disable id FormControl', () => {
        const formGroup = service.createProcReqMasterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
