import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../salary-generator-master.test-samples';

import { SalaryGeneratorMasterFormService } from './salary-generator-master-form.service';

describe('SalaryGeneratorMaster Form Service', () => {
  let service: SalaryGeneratorMasterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryGeneratorMasterFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryGeneratorMasterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            isGenerated: expect.any(Object),
            isMobileBillImported: expect.any(Object),
            isPFLoanRepaymentImported: expect.any(Object),
            isAttendanceImported: expect.any(Object),
            isSalaryDeductionImported: expect.any(Object),
            isFinalized: expect.any(Object),
            visibility: expect.any(Object),
          })
        );
      });

      it('passing ISalaryGeneratorMaster should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            isGenerated: expect.any(Object),
            isMobileBillImported: expect.any(Object),
            isPFLoanRepaymentImported: expect.any(Object),
            isAttendanceImported: expect.any(Object),
            isSalaryDeductionImported: expect.any(Object),
            isFinalized: expect.any(Object),
            visibility: expect.any(Object),
          })
        );
      });
    });

    describe('getSalaryGeneratorMaster', () => {
      it('should return NewSalaryGeneratorMaster for default SalaryGeneratorMaster initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSalaryGeneratorMasterFormGroup(sampleWithNewData);

        const salaryGeneratorMaster = service.getSalaryGeneratorMaster(formGroup) as any;

        expect(salaryGeneratorMaster).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalaryGeneratorMaster for empty SalaryGeneratorMaster initial value', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup();

        const salaryGeneratorMaster = service.getSalaryGeneratorMaster(formGroup) as any;

        expect(salaryGeneratorMaster).toMatchObject({});
      });

      it('should return ISalaryGeneratorMaster', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup(sampleWithRequiredData);

        const salaryGeneratorMaster = service.getSalaryGeneratorMaster(formGroup) as any;

        expect(salaryGeneratorMaster).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalaryGeneratorMaster should not enable id FormControl', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalaryGeneratorMaster should disable id FormControl', () => {
        const formGroup = service.createSalaryGeneratorMasterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
