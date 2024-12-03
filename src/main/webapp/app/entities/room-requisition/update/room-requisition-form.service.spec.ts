import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../room-requisition.test-samples';

import { RoomRequisitionFormService } from './room-requisition-form.service';

describe('RoomRequisition Form Service', () => {
  let service: RoomRequisitionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoomRequisitionFormService);
  });

  describe('Service methods', () => {
    describe('createRoomRequisitionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoomRequisitionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            bookingTrn: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionedAt: expect.any(Object),
            participantList: expect.any(Object),
            rejectedReason: expect.any(Object),
            bookingStartDate: expect.any(Object),
            bookingEndDate: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            title: expect.any(Object),
            agenda: expect.any(Object),
            optionalParticipantList: expect.any(Object),
            isFullDay: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionedBy: expect.any(Object),
            requester: expect.any(Object),
            room: expect.any(Object),
          })
        );
      });

      it('passing IRoomRequisition should create a new form with FormGroup', () => {
        const formGroup = service.createRoomRequisitionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            bookingTrn: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionedAt: expect.any(Object),
            participantList: expect.any(Object),
            rejectedReason: expect.any(Object),
            bookingStartDate: expect.any(Object),
            bookingEndDate: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            title: expect.any(Object),
            agenda: expect.any(Object),
            optionalParticipantList: expect.any(Object),
            isFullDay: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            sanctionedBy: expect.any(Object),
            requester: expect.any(Object),
            room: expect.any(Object),
          })
        );
      });
    });

    describe('getRoomRequisition', () => {
      it('should return NewRoomRequisition for default RoomRequisition initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRoomRequisitionFormGroup(sampleWithNewData);

        const roomRequisition = service.getRoomRequisition(formGroup) as any;

        expect(roomRequisition).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoomRequisition for empty RoomRequisition initial value', () => {
        const formGroup = service.createRoomRequisitionFormGroup();

        const roomRequisition = service.getRoomRequisition(formGroup) as any;

        expect(roomRequisition).toMatchObject({});
      });

      it('should return IRoomRequisition', () => {
        const formGroup = service.createRoomRequisitionFormGroup(sampleWithRequiredData);

        const roomRequisition = service.getRoomRequisition(formGroup) as any;

        expect(roomRequisition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoomRequisition should not enable id FormControl', () => {
        const formGroup = service.createRoomRequisitionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoomRequisition should disable id FormControl', () => {
        const formGroup = service.createRoomRequisitionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
