import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../room-type.test-samples';

import { RoomTypeFormService } from './room-type-form.service';

describe('RoomType Form Service', () => {
  let service: RoomTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoomTypeFormService);
  });

  describe('Service methods', () => {
    describe('createRoomTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoomTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeName: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IRoomType should create a new form with FormGroup', () => {
        const formGroup = service.createRoomTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeName: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getRoomType', () => {
      it('should return NewRoomType for default RoomType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRoomTypeFormGroup(sampleWithNewData);

        const roomType = service.getRoomType(formGroup) as any;

        expect(roomType).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoomType for empty RoomType initial value', () => {
        const formGroup = service.createRoomTypeFormGroup();

        const roomType = service.getRoomType(formGroup) as any;

        expect(roomType).toMatchObject({});
      });

      it('should return IRoomType', () => {
        const formGroup = service.createRoomTypeFormGroup(sampleWithRequiredData);

        const roomType = service.getRoomType(formGroup) as any;

        expect(roomType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoomType should not enable id FormControl', () => {
        const formGroup = service.createRoomTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoomType should disable id FormControl', () => {
        const formGroup = service.createRoomTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
