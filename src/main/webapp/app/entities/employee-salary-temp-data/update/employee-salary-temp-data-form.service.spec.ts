import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-salary-temp-data.test-samples';

import { EmployeeSalaryTempDataFormService } from './employee-salary-temp-data-form.service';

describe('EmployeeSalaryTempData Form Service', () => {
  let service: EmployeeSalaryTempDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeSalaryTempDataFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeSalaryTempDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            mainGrossSalary: expect.any(Object),
            mainGrossBasicSalary: expect.any(Object),
            mainGrossHouseRent: expect.any(Object),
            mainGrossMedicalAllowance: expect.any(Object),
            mainGrossConveyanceAllowance: expect.any(Object),
            absentDays: expect.any(Object),
            fractionDays: expect.any(Object),
            payableGrossSalary: expect.any(Object),
            payableGrossBasicSalary: expect.any(Object),
            payableGrossHouseRent: expect.any(Object),
            payableGrossMedicalAllowance: expect.any(Object),
            payableGrossConveyanceAllowance: expect.any(Object),
            arrearSalary: expect.any(Object),
            pfDeduction: expect.any(Object),
            taxDeduction: expect.any(Object),
            welfareFundDeduction: expect.any(Object),
            mobileBillDeduction: expect.any(Object),
            otherDeduction: expect.any(Object),
            totalDeduction: expect.any(Object),
            netPay: expect.any(Object),
            remarks: expect.any(Object),
            pfContribution: expect.any(Object),
            gfContribution: expect.any(Object),
            provisionForFestivalBonus: expect.any(Object),
            provisionForLeaveEncashment: expect.any(Object),
            provishionForProjectBonus: expect.any(Object),
            livingAllowance: expect.any(Object),
            otherAddition: expect.any(Object),
            salaryAdjustment: expect.any(Object),
            providentFundArrear: expect.any(Object),
            entertainment: expect.any(Object),
            utility: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeSalaryTempData should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            mainGrossSalary: expect.any(Object),
            mainGrossBasicSalary: expect.any(Object),
            mainGrossHouseRent: expect.any(Object),
            mainGrossMedicalAllowance: expect.any(Object),
            mainGrossConveyanceAllowance: expect.any(Object),
            absentDays: expect.any(Object),
            fractionDays: expect.any(Object),
            payableGrossSalary: expect.any(Object),
            payableGrossBasicSalary: expect.any(Object),
            payableGrossHouseRent: expect.any(Object),
            payableGrossMedicalAllowance: expect.any(Object),
            payableGrossConveyanceAllowance: expect.any(Object),
            arrearSalary: expect.any(Object),
            pfDeduction: expect.any(Object),
            taxDeduction: expect.any(Object),
            welfareFundDeduction: expect.any(Object),
            mobileBillDeduction: expect.any(Object),
            otherDeduction: expect.any(Object),
            totalDeduction: expect.any(Object),
            netPay: expect.any(Object),
            remarks: expect.any(Object),
            pfContribution: expect.any(Object),
            gfContribution: expect.any(Object),
            provisionForFestivalBonus: expect.any(Object),
            provisionForLeaveEncashment: expect.any(Object),
            provishionForProjectBonus: expect.any(Object),
            livingAllowance: expect.any(Object),
            otherAddition: expect.any(Object),
            salaryAdjustment: expect.any(Object),
            providentFundArrear: expect.any(Object),
            entertainment: expect.any(Object),
            utility: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeSalaryTempData', () => {
      it('should return NewEmployeeSalaryTempData for default EmployeeSalaryTempData initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeSalaryTempDataFormGroup(sampleWithNewData);

        const employeeSalaryTempData = service.getEmployeeSalaryTempData(formGroup) as any;

        expect(employeeSalaryTempData).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeSalaryTempData for empty EmployeeSalaryTempData initial value', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup();

        const employeeSalaryTempData = service.getEmployeeSalaryTempData(formGroup) as any;

        expect(employeeSalaryTempData).toMatchObject({});
      });

      it('should return IEmployeeSalaryTempData', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup(sampleWithRequiredData);

        const employeeSalaryTempData = service.getEmployeeSalaryTempData(formGroup) as any;

        expect(employeeSalaryTempData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeSalaryTempData should not enable id FormControl', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeSalaryTempData should disable id FormControl', () => {
        const formGroup = service.createEmployeeSalaryTempDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
