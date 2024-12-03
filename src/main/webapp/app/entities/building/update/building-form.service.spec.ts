import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../building.test-samples';

import { BuildingFormService } from './building-form.service';

describe('Building Form Service', () => {
  let service: BuildingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BuildingFormService);
  });

  describe('Service methods', () => {
    describe('createBuildingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBuildingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            buildingName: expect.any(Object),
            buildingLocation: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IBuilding should create a new form with FormGroup', () => {
        const formGroup = service.createBuildingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            buildingName: expect.any(Object),
            buildingLocation: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getBuilding', () => {
      it('should return NewBuilding for default Building initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBuildingFormGroup(sampleWithNewData);

        const building = service.getBuilding(formGroup) as any;

        expect(building).toMatchObject(sampleWithNewData);
      });

      it('should return NewBuilding for empty Building initial value', () => {
        const formGroup = service.createBuildingFormGroup();

        const building = service.getBuilding(formGroup) as any;

        expect(building).toMatchObject({});
      });

      it('should return IBuilding', () => {
        const formGroup = service.createBuildingFormGroup(sampleWithRequiredData);

        const building = service.getBuilding(formGroup) as any;

        expect(building).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBuilding should not enable id FormControl', () => {
        const formGroup = service.createBuildingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBuilding should disable id FormControl', () => {
        const formGroup = service.createBuildingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
