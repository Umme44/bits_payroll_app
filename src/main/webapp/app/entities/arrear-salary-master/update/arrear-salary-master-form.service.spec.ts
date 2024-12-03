import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../arrear-salary-master.test-samples';

import { ArrearSalaryMasterFormService } from './arrear-salary-master-form.service';

describe('ArrearSalaryMaster Form Service', () => {
  let service: ArrearSalaryMasterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArrearSalaryMasterFormService);
  });

  describe('Service methods', () => {
    describe('createArrearSalaryMasterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            isLocked: expect.any(Object),
            isDeleted: expect.any(Object),
          })
        );
      });

      it('passing IArrearSalaryMaster should create a new form with FormGroup', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            isLocked: expect.any(Object),
            isDeleted: expect.any(Object),
          })
        );
      });
    });

    describe('getArrearSalaryMaster', () => {
      it('should return NewArrearSalaryMaster for default ArrearSalaryMaster initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createArrearSalaryMasterFormGroup(sampleWithNewData);

        const arrearSalaryMaster = service.getArrearSalaryMaster(formGroup) as any;

        expect(arrearSalaryMaster).toMatchObject(sampleWithNewData);
      });

      it('should return NewArrearSalaryMaster for empty ArrearSalaryMaster initial value', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup();

        const arrearSalaryMaster = service.getArrearSalaryMaster(formGroup) as any;

        expect(arrearSalaryMaster).toMatchObject({});
      });

      it('should return IArrearSalaryMaster', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup(sampleWithRequiredData);

        const arrearSalaryMaster = service.getArrearSalaryMaster(formGroup) as any;

        expect(arrearSalaryMaster).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArrearSalaryMaster should not enable id FormControl', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArrearSalaryMaster should disable id FormControl', () => {
        const formGroup = service.createArrearSalaryMasterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
