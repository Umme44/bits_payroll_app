import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-salary.test-samples';

import { EmployeeSalaryFormService } from './employee-salary-form.service';

describe('EmployeeSalary Form Service', () => {
  let service: EmployeeSalaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeSalaryFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeSalaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeSalaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            salaryGenerationDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            refPin: expect.any(Object),
            pin: expect.any(Object),
            joiningDate: expect.any(Object),
            confirmationDate: expect.any(Object),
            employeeCategory: expect.any(Object),
            unit: expect.any(Object),
            department: expect.any(Object),
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
            isFinalized: expect.any(Object),
            isDispatched: expect.any(Object),
            entertainment: expect.any(Object),
            utility: expect.any(Object),
            otherAddition: expect.any(Object),
            salaryAdjustment: expect.any(Object),
            providentFundArrear: expect.any(Object),
            allowance01: expect.any(Object),
            allowance02: expect.any(Object),
            allowance03: expect.any(Object),
            allowance04: expect.any(Object),
            allowance05: expect.any(Object),
            allowance06: expect.any(Object),
            provisionForProjectBonus: expect.any(Object),
            isHold: expect.any(Object),
            attendanceRegularisationStartDate: expect.any(Object),
            attendanceRegularisationEndDate: expect.any(Object),
            title: expect.any(Object),
            isVisibleToEmployee: expect.any(Object),
            pfArrear: expect.any(Object),
            taxCalculationSnapshot: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeSalary should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeSalaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            salaryGenerationDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdAt: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedAt: expect.any(Object),
            refPin: expect.any(Object),
            pin: expect.any(Object),
            joiningDate: expect.any(Object),
            confirmationDate: expect.any(Object),
            employeeCategory: expect.any(Object),
            unit: expect.any(Object),
            department: expect.any(Object),
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
            isFinalized: expect.any(Object),
            isDispatched: expect.any(Object),
            entertainment: expect.any(Object),
            utility: expect.any(Object),
            otherAddition: expect.any(Object),
            salaryAdjustment: expect.any(Object),
            providentFundArrear: expect.any(Object),
            allowance01: expect.any(Object),
            allowance02: expect.any(Object),
            allowance03: expect.any(Object),
            allowance04: expect.any(Object),
            allowance05: expect.any(Object),
            allowance06: expect.any(Object),
            provisionForProjectBonus: expect.any(Object),
            isHold: expect.any(Object),
            attendanceRegularisationStartDate: expect.any(Object),
            attendanceRegularisationEndDate: expect.any(Object),
            title: expect.any(Object),
            isVisibleToEmployee: expect.any(Object),
            pfArrear: expect.any(Object),
            taxCalculationSnapshot: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeSalary', () => {
      it('should return NewEmployeeSalary for default EmployeeSalary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeSalaryFormGroup(sampleWithNewData);

        const employeeSalary = service.getEmployeeSalary(formGroup) as any;

        expect(employeeSalary).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeSalary for empty EmployeeSalary initial value', () => {
        const formGroup = service.createEmployeeSalaryFormGroup();

        const employeeSalary = service.getEmployeeSalary(formGroup) as any;

        expect(employeeSalary).toMatchObject({});
      });

      it('should return IEmployeeSalary', () => {
        const formGroup = service.createEmployeeSalaryFormGroup(sampleWithRequiredData);

        const employeeSalary = service.getEmployeeSalary(formGroup) as any;

        expect(employeeSalary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeSalary should not enable id FormControl', () => {
        const formGroup = service.createEmployeeSalaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeSalary should disable id FormControl', () => {
        const formGroup = service.createEmployeeSalaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
