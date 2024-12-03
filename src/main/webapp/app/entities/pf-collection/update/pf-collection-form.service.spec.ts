import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-collection.test-samples';

import { PfCollectionFormService } from './pf-collection-form.service';

describe('PfCollection Form Service', () => {
  let service: PfCollectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfCollectionFormService);
  });

  describe('Service methods', () => {
    describe('createPfCollectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfCollectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeContribution: expect.any(Object),
            employerContribution: expect.any(Object),
            transactionDate: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            collectionType: expect.any(Object),
            employeeInterest: expect.any(Object),
            employerInterest: expect.any(Object),
            gross: expect.any(Object),
            basic: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });

      it('passing IPfCollection should create a new form with FormGroup', () => {
        const formGroup = service.createPfCollectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeContribution: expect.any(Object),
            employerContribution: expect.any(Object),
            transactionDate: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            collectionType: expect.any(Object),
            employeeInterest: expect.any(Object),
            employerInterest: expect.any(Object),
            gross: expect.any(Object),
            basic: expect.any(Object),
            pfAccount: expect.any(Object),
          })
        );
      });
    });

    describe('getPfCollection', () => {
      it('should return NewPfCollection for default PfCollection initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfCollectionFormGroup(sampleWithNewData);

        const pfCollection = service.getPfCollection(formGroup) as any;

        expect(pfCollection).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfCollection for empty PfCollection initial value', () => {
        const formGroup = service.createPfCollectionFormGroup();

        const pfCollection = service.getPfCollection(formGroup) as any;

        expect(pfCollection).toMatchObject({});
      });

      it('should return IPfCollection', () => {
        const formGroup = service.createPfCollectionFormGroup(sampleWithRequiredData);

        const pfCollection = service.getPfCollection(formGroup) as any;

        expect(pfCollection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfCollection should not enable id FormControl', () => {
        const formGroup = service.createPfCollectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfCollection should disable id FormControl', () => {
        const formGroup = service.createPfCollectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
