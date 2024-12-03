import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../nationality.test-samples';

import { NationalityFormService } from './nationality-form.service';

describe('Nationality Form Service', () => {
  let service: NationalityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NationalityFormService);
  });

  describe('Service methods', () => {
    describe('createNationalityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNationalityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nationalityName: expect.any(Object),
          })
        );
      });

      it('passing INationality should create a new form with FormGroup', () => {
        const formGroup = service.createNationalityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nationalityName: expect.any(Object),
          })
        );
      });
    });

    describe('getNationality', () => {
      it('should return NewNationality for default Nationality initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNationalityFormGroup(sampleWithNewData);

        const nationality = service.getNationality(formGroup) as any;

        expect(nationality).toMatchObject(sampleWithNewData);
      });

      it('should return NewNationality for empty Nationality initial value', () => {
        const formGroup = service.createNationalityFormGroup();

        const nationality = service.getNationality(formGroup) as any;

        expect(nationality).toMatchObject({});
      });

      it('should return INationality', () => {
        const formGroup = service.createNationalityFormGroup(sampleWithRequiredData);

        const nationality = service.getNationality(formGroup) as any;

        expect(nationality).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INationality should not enable id FormControl', () => {
        const formGroup = service.createNationalityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNationality should disable id FormControl', () => {
        const formGroup = service.createNationalityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
