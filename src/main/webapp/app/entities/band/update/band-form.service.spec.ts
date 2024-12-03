import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../band.test-samples';

import { BandFormService } from './band-form.service';

describe('Band Form Service', () => {
  let service: BandFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BandFormService);
  });

  describe('Service methods', () => {
    describe('createBandFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBandFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bandName: expect.any(Object),
            minSalary: expect.any(Object),
            maxSalary: expect.any(Object),
            welfareFund: expect.any(Object),
            mobileCelling: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IBand should create a new form with FormGroup', () => {
        const formGroup = service.createBandFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bandName: expect.any(Object),
            minSalary: expect.any(Object),
            maxSalary: expect.any(Object),
            welfareFund: expect.any(Object),
            mobileCelling: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getBand', () => {
      it('should return NewBand for default Band initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBandFormGroup(sampleWithNewData);

        const band = service.getBand(formGroup) as any;

        expect(band).toMatchObject(sampleWithNewData);
      });

      it('should return NewBand for empty Band initial value', () => {
        const formGroup = service.createBandFormGroup();

        const band = service.getBand(formGroup) as any;

        expect(band).toMatchObject({});
      });

      it('should return IBand', () => {
        const formGroup = service.createBandFormGroup(sampleWithRequiredData);

        const band = service.getBand(formGroup) as any;

        expect(band).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBand should not enable id FormControl', () => {
        const formGroup = service.createBandFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBand should disable id FormControl', () => {
        const formGroup = service.createBandFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
