import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../unit-of-measurement.test-samples';

import { UnitOfMeasurementFormService } from './unit-of-measurement-form.service';

describe('UnitOfMeasurement Form Service', () => {
  let service: UnitOfMeasurementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UnitOfMeasurementFormService);
  });

  describe('Service methods', () => {
    describe('createUnitOfMeasurementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IUnitOfMeasurement should create a new form with FormGroup', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getUnitOfMeasurement', () => {
      it('should return NewUnitOfMeasurement for default UnitOfMeasurement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUnitOfMeasurementFormGroup(sampleWithNewData);

        const unitOfMeasurement = service.getUnitOfMeasurement(formGroup) as any;

        expect(unitOfMeasurement).toMatchObject(sampleWithNewData);
      });

      it('should return NewUnitOfMeasurement for empty UnitOfMeasurement initial value', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup();

        const unitOfMeasurement = service.getUnitOfMeasurement(formGroup) as any;

        expect(unitOfMeasurement).toMatchObject({});
      });

      it('should return IUnitOfMeasurement', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup(sampleWithRequiredData);

        const unitOfMeasurement = service.getUnitOfMeasurement(formGroup) as any;

        expect(unitOfMeasurement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUnitOfMeasurement should not enable id FormControl', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUnitOfMeasurement should disable id FormControl', () => {
        const formGroup = service.createUnitOfMeasurementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
