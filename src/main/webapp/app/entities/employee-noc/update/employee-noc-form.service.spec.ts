import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-noc.test-samples';

import { EmployeeNOCFormService } from './employee-noc-form.service';

describe('EmployeeNOC Form Service', () => {
  let service: EmployeeNOCFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeNOCFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeNOCFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeNOCFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            passportNumber: expect.any(Object),
            leaveStartDate: expect.any(Object),
            leaveEndDate: expect.any(Object),
            countryToVisit: expect.any(Object),
            referenceNumber: expect.any(Object),
            issueDate: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            generatedAt: expect.any(Object),
            reason: expect.any(Object),
            purposeOfNOC: expect.any(Object),
            certificateStatus: expect.any(Object),
            isRequiredForVisa: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            generatedBy: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeNOC should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeNOCFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            passportNumber: expect.any(Object),
            leaveStartDate: expect.any(Object),
            leaveEndDate: expect.any(Object),
            countryToVisit: expect.any(Object),
            referenceNumber: expect.any(Object),
            issueDate: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            generatedAt: expect.any(Object),
            reason: expect.any(Object),
            purposeOfNOC: expect.any(Object),
            certificateStatus: expect.any(Object),
            isRequiredForVisa: expect.any(Object),
            employee: expect.any(Object),
            signatoryPerson: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            generatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeNOC', () => {
      it('should return NewEmployeeNOC for default EmployeeNOC initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeNOCFormGroup(sampleWithNewData);

        const employeeNOC = service.getEmployeeNOC(formGroup) as any;

        expect(employeeNOC).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeNOC for empty EmployeeNOC initial value', () => {
        const formGroup = service.createEmployeeNOCFormGroup();

        const employeeNOC = service.getEmployeeNOC(formGroup) as any;

        expect(employeeNOC).toMatchObject({});
      });

      it('should return IEmployeeNOC', () => {
        const formGroup = service.createEmployeeNOCFormGroup(sampleWithRequiredData);

        const employeeNOC = service.getEmployeeNOC(formGroup) as any;

        expect(employeeNOC).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeNOC should not enable id FormControl', () => {
        const formGroup = service.createEmployeeNOCFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeNOC should disable id FormControl', () => {
        const formGroup = service.createEmployeeNOCFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
