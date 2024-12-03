import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hold-salary-disbursement.test-samples';

import { HoldSalaryDisbursementFormService } from './hold-salary-disbursement-form.service';

describe('HoldSalaryDisbursement Form Service', () => {
  let service: HoldSalaryDisbursementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HoldSalaryDisbursementFormService);
  });

  describe('Service methods', () => {
    describe('createHoldSalaryDisbursementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            user: expect.any(Object),
            employeeSalary: expect.any(Object),
          })
        );
      });

      it('passing IHoldSalaryDisbursement should create a new form with FormGroup', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            user: expect.any(Object),
            employeeSalary: expect.any(Object),
          })
        );
      });
    });

    describe('getHoldSalaryDisbursement', () => {
      it('should return NewHoldSalaryDisbursement for default HoldSalaryDisbursement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHoldSalaryDisbursementFormGroup(sampleWithNewData);

        const holdSalaryDisbursement = service.getHoldSalaryDisbursement(formGroup) as any;

        expect(holdSalaryDisbursement).toMatchObject(sampleWithNewData);
      });

      it('should return NewHoldSalaryDisbursement for empty HoldSalaryDisbursement initial value', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup();

        const holdSalaryDisbursement = service.getHoldSalaryDisbursement(formGroup) as any;

        expect(holdSalaryDisbursement).toMatchObject({});
      });

      it('should return IHoldSalaryDisbursement', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup(sampleWithRequiredData);

        const holdSalaryDisbursement = service.getHoldSalaryDisbursement(formGroup) as any;

        expect(holdSalaryDisbursement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHoldSalaryDisbursement should not enable id FormControl', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHoldSalaryDisbursement should disable id FormControl', () => {
        const formGroup = service.createHoldSalaryDisbursementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
