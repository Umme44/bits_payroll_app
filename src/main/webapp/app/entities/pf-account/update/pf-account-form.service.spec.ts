import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-account.test-samples';

import { PfAccountFormService } from './pf-account-form.service';

describe('PfAccount Form Service', () => {
  let service: PfAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfAccountFormService);
  });

  describe('Service methods', () => {
    describe('createPfAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pfCode: expect.any(Object),
            membershipStartDate: expect.any(Object),
            membershipEndDate: expect.any(Object),
            status: expect.any(Object),
            designationName: expect.any(Object),
            departmentName: expect.any(Object),
            unitName: expect.any(Object),
            accHolderName: expect.any(Object),
            pin: expect.any(Object),
            dateOfJoining: expect.any(Object),
            dateOfConfirmation: expect.any(Object),
          })
        );
      });

      it('passing IPfAccount should create a new form with FormGroup', () => {
        const formGroup = service.createPfAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pfCode: expect.any(Object),
            membershipStartDate: expect.any(Object),
            membershipEndDate: expect.any(Object),
            status: expect.any(Object),
            designationName: expect.any(Object),
            departmentName: expect.any(Object),
            unitName: expect.any(Object),
            accHolderName: expect.any(Object),
            pin: expect.any(Object),
            dateOfJoining: expect.any(Object),
            dateOfConfirmation: expect.any(Object),
          })
        );
      });
    });

    describe('getPfAccount', () => {
      it('should return NewPfAccount for default PfAccount initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfAccountFormGroup(sampleWithNewData);

        const pfAccount = service.getPfAccount(formGroup) as any;

        expect(pfAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfAccount for empty PfAccount initial value', () => {
        const formGroup = service.createPfAccountFormGroup();

        const pfAccount = service.getPfAccount(formGroup) as any;

        expect(pfAccount).toMatchObject({});
      });

      it('should return IPfAccount', () => {
        const formGroup = service.createPfAccountFormGroup(sampleWithRequiredData);

        const pfAccount = service.getPfAccount(formGroup) as any;

        expect(pfAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfAccount should not enable id FormControl', () => {
        const formGroup = service.createPfAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfAccount should disable id FormControl', () => {
        const formGroup = service.createPfAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
