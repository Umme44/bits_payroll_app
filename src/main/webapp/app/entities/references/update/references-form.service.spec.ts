import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../references.test-samples';

import { ReferencesFormService } from './references-form.service';

describe('References Form Service', () => {
  let service: ReferencesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReferencesFormService);
  });

  describe('Service methods', () => {
    describe('createReferencesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReferencesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            institute: expect.any(Object),
            designation: expect.any(Object),
            relationshipWithEmployee: expect.any(Object),
            email: expect.any(Object),
            contactNumber: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IReferences should create a new form with FormGroup', () => {
        const formGroup = service.createReferencesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            institute: expect.any(Object),
            designation: expect.any(Object),
            relationshipWithEmployee: expect.any(Object),
            email: expect.any(Object),
            contactNumber: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getReferences', () => {
      it('should return NewReferences for default References initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createReferencesFormGroup(sampleWithNewData);

        const references = service.getReferences(formGroup) as any;

        expect(references).toMatchObject(sampleWithNewData);
      });

      it('should return NewReferences for empty References initial value', () => {
        const formGroup = service.createReferencesFormGroup();

        const references = service.getReferences(formGroup) as any;

        expect(references).toMatchObject({});
      });

      it('should return IReferences', () => {
        const formGroup = service.createReferencesFormGroup(sampleWithRequiredData);

        const references = service.getReferences(formGroup) as any;

        expect(references).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReferences should not enable id FormControl', () => {
        const formGroup = service.createReferencesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReferences should disable id FormControl', () => {
        const formGroup = service.createReferencesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
