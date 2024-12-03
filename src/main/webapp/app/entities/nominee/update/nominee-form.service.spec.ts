import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../nominee.test-samples';

import { NomineeFormService } from './nominee-form.service';

describe('Nominee Form Service', () => {
  let service: NomineeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NomineeFormService);
  });

  describe('Service methods', () => {
    describe('createNomineeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNomineeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomineeName: expect.any(Object),
            presentAddress: expect.any(Object),
            relationshipWithEmployee: expect.any(Object),
            dateOfBirth: expect.any(Object),
            age: expect.any(Object),
            sharePercentage: expect.any(Object),
            imagePath: expect.any(Object),
            status: expect.any(Object),
            guardianName: expect.any(Object),
            guardianFatherName: expect.any(Object),
            guardianSpouseName: expect.any(Object),
            guardianDateOfBirth: expect.any(Object),
            guardianPresentAddress: expect.any(Object),
            guardianDocumentName: expect.any(Object),
            guardianRelationshipWith: expect.any(Object),
            guardianImagePath: expect.any(Object),
            isLocked: expect.any(Object),
            nominationDate: expect.any(Object),
            permanentAddress: expect.any(Object),
            guardianPermanentAddress: expect.any(Object),
            nomineeType: expect.any(Object),
            identityType: expect.any(Object),
            documentName: expect.any(Object),
            idNumber: expect.any(Object),
            isNidVerified: expect.any(Object),
            guardianIdentityType: expect.any(Object),
            guardianIdNumber: expect.any(Object),
            isGuardianNidVerified: expect.any(Object),
            employee: expect.any(Object),
            approvedBy: expect.any(Object),
            witness: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });

      it('passing INominee should create a new form with FormGroup', () => {
        const formGroup = service.createNomineeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomineeName: expect.any(Object),
            presentAddress: expect.any(Object),
            relationshipWithEmployee: expect.any(Object),
            dateOfBirth: expect.any(Object),
            age: expect.any(Object),
            sharePercentage: expect.any(Object),
            imagePath: expect.any(Object),
            status: expect.any(Object),
            guardianName: expect.any(Object),
            guardianFatherName: expect.any(Object),
            guardianSpouseName: expect.any(Object),
            guardianDateOfBirth: expect.any(Object),
            guardianPresentAddress: expect.any(Object),
            guardianDocumentName: expect.any(Object),
            guardianRelationshipWith: expect.any(Object),
            guardianImagePath: expect.any(Object),
            isLocked: expect.any(Object),
            nominationDate: expect.any(Object),
            permanentAddress: expect.any(Object),
            guardianPermanentAddress: expect.any(Object),
            nomineeType: expect.any(Object),
            identityType: expect.any(Object),
            documentName: expect.any(Object),
            idNumber: expect.any(Object),
            isNidVerified: expect.any(Object),
            guardianIdentityType: expect.any(Object),
            guardianIdNumber: expect.any(Object),
            isGuardianNidVerified: expect.any(Object),
            employee: expect.any(Object),
            approvedBy: expect.any(Object),
            witness: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });
    });

    describe('getNominee', () => {
      it('should return NewNominee for default Nominee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNomineeFormGroup(sampleWithNewData);

        const nominee = service.getNominee(formGroup) as any;

        expect(nominee).toMatchObject(sampleWithNewData);
      });

      it('should return NewNominee for empty Nominee initial value', () => {
        const formGroup = service.createNomineeFormGroup();

        const nominee = service.getNominee(formGroup) as any;

        expect(nominee).toMatchObject({});
      });

      it('should return INominee', () => {
        const formGroup = service.createNomineeFormGroup(sampleWithRequiredData);

        const nominee = service.getNominee(formGroup) as any;

        expect(nominee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INominee should not enable id FormControl', () => {
        const formGroup = service.createNomineeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNominee should disable id FormControl', () => {
        const formGroup = service.createNomineeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
