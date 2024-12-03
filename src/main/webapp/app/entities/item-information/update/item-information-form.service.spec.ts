import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../item-information.test-samples';

import { ItemInformationFormService } from './item-information-form.service';

describe('ItemInformation Form Service', () => {
  let service: ItemInformationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemInformationFormService);
  });

  describe('Service methods', () => {
    describe('createItemInformationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createItemInformationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            specification: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            unitOfMeasurement: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IItemInformation should create a new form with FormGroup', () => {
        const formGroup = service.createItemInformationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            specification: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            department: expect.any(Object),
            unitOfMeasurement: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getItemInformation', () => {
      it('should return NewItemInformation for default ItemInformation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createItemInformationFormGroup(sampleWithNewData);

        const itemInformation = service.getItemInformation(formGroup) as any;

        expect(itemInformation).toMatchObject(sampleWithNewData);
      });

      it('should return NewItemInformation for empty ItemInformation initial value', () => {
        const formGroup = service.createItemInformationFormGroup();

        const itemInformation = service.getItemInformation(formGroup) as any;

        expect(itemInformation).toMatchObject({});
      });

      it('should return IItemInformation', () => {
        const formGroup = service.createItemInformationFormGroup(sampleWithRequiredData);

        const itemInformation = service.getItemInformation(formGroup) as any;

        expect(itemInformation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IItemInformation should not enable id FormControl', () => {
        const formGroup = service.createItemInformationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewItemInformation should disable id FormControl', () => {
        const formGroup = service.createItemInformationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
