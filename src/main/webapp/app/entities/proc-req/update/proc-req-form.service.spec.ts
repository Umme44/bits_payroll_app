import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../proc-req.test-samples';

import { ProcReqMasterFormService } from './proc-req-master-form.service';

describe('ProcReq Form Service', () => {
  let service: ProcReqMasterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcReqMasterFormService);
  });

  describe('Service methods', () => {
    describe('createProcReqFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProcReqFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            referenceFilePath: expect.any(Object),
            itemInformation: expect.any(Object),
            procReqMaster: expect.any(Object),
          })
        );
      });

      it('passing IProcReq should create a new form with FormGroup', () => {
        const formGroup = service.createProcReqFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            referenceFilePath: expect.any(Object),
            itemInformation: expect.any(Object),
            procReqMaster: expect.any(Object),
          })
        );
      });
    });

    describe('getProcReq', () => {
      it('should return NewProcReq for default ProcReq initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProcReqFormGroup(sampleWithNewData);

        const procReq = service.getProcReq(formGroup) as any;

        expect(procReq).toMatchObject(sampleWithNewData);
      });

      it('should return NewProcReq for empty ProcReq initial value', () => {
        const formGroup = service.createProcReqFormGroup();

        const procReq = service.getProcReq(formGroup) as any;

        expect(procReq).toMatchObject({});
      });

      it('should return IProcReq', () => {
        const formGroup = service.createProcReqFormGroup(sampleWithRequiredData);

        const procReq = service.getProcReq(formGroup) as any;

        expect(procReq).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProcReq should not enable id FormControl', () => {
        const formGroup = service.createProcReqFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProcReq should disable id FormControl', () => {
        const formGroup = service.createProcReqFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
