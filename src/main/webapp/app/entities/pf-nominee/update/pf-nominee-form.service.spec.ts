import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-nominee.test-samples';

import { PfNomineeFormService } from './pf-nominee-form.service';

describe('PfNominee Form Service', () => {
  let service: PfNomineeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfNomineeFormService);
  });

  describe('Service methods', () => {
    describe('createPfNomineeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfNomineeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nominationDate: expect.any(Object),
            fullName: expect.any(Object),
            presentAddress: expect.any(Object),
            permanentAddress: expect.any(Object),
            relationship: expect.any(Object),
            dateOfBirth: expect.any(Object),
            age: expect.any(Object),
            sharePercentage: expect.any(Object),
            nidNumber: expect.any(Object),
            isNidVerified: expect.any(Object),
            passportNumber: expect.any(Object),
            brnNumber: expect.any(Object),
            photo: expect.any(Object),
            guardianName: expect.any(Object),
            guardianFatherOrSpouseName: expect.any(Object),
            guardianDateOfBirth: expect.any(Object),
            guardianPresentAddress: expect.any(Object),
            guardianPermanentAddress: expect.any(Object),
            guardianProofOfIdentityOfLegalGuardian: expect.any(Object),
            guardianRelationshipWithNominee: expect.any(Object),
            guardianNidNumber: expect.any(Object),
            guardianBrnNumber: expect.any(Object),
            guardianIdNumber: expect.any(Object),
            isGuardianNidVerified: expect.any(Object),
            isApproved: expect.any(Object),
            identityType: expect.any(Object),
            idNumber: expect.any(Object),
            documentName: expect.any(Object),
            guardianIdentityType: expect.any(Object),
            guardianDocumentName: expect.any(Object),
            pfAccount: expect.any(Object),
            pfWitness: expect.any(Object),
            approvedBy: expect.any(Object),
          })
        );
      });

      it('passing IPfNominee should create a new form with FormGroup', () => {
        const formGroup = service.createPfNomineeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nominationDate: expect.any(Object),
            fullName: expect.any(Object),
            presentAddress: expect.any(Object),
            permanentAddress: expect.any(Object),
            relationship: expect.any(Object),
            dateOfBirth: expect.any(Object),
            age: expect.any(Object),
            sharePercentage: expect.any(Object),
            nidNumber: expect.any(Object),
            isNidVerified: expect.any(Object),
            passportNumber: expect.any(Object),
            brnNumber: expect.any(Object),
            photo: expect.any(Object),
            guardianName: expect.any(Object),
            guardianFatherOrSpouseName: expect.any(Object),
            guardianDateOfBirth: expect.any(Object),
            guardianPresentAddress: expect.any(Object),
            guardianPermanentAddress: expect.any(Object),
            guardianProofOfIdentityOfLegalGuardian: expect.any(Object),
            guardianRelationshipWithNominee: expect.any(Object),
            guardianNidNumber: expect.any(Object),
            guardianBrnNumber: expect.any(Object),
            guardianIdNumber: expect.any(Object),
            isGuardianNidVerified: expect.any(Object),
            isApproved: expect.any(Object),
            identityType: expect.any(Object),
            idNumber: expect.any(Object),
            documentName: expect.any(Object),
            guardianIdentityType: expect.any(Object),
            guardianDocumentName: expect.any(Object),
            pfAccount: expect.any(Object),
            pfWitness: expect.any(Object),
            approvedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getPfNominee', () => {
      it('should return NewPfNominee for default PfNominee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfNomineeFormGroup(sampleWithNewData);

        const pfNominee = service.getPfNominee(formGroup) as any;

        expect(pfNominee).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfNominee for empty PfNominee initial value', () => {
        const formGroup = service.createPfNomineeFormGroup();

        const pfNominee = service.getPfNominee(formGroup) as any;

        expect(pfNominee).toMatchObject({});
      });

      it('should return IPfNominee', () => {
        const formGroup = service.createPfNomineeFormGroup(sampleWithRequiredData);

        const pfNominee = service.getPfNominee(formGroup) as any;

        expect(pfNominee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfNominee should not enable id FormControl', () => {
        const formGroup = service.createPfNomineeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfNominee should disable id FormControl', () => {
        const formGroup = service.createPfNomineeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
