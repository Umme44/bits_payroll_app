import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vehicle-requisition.test-samples';

import { VehicleRequisitionFormService } from './vehicle-requisition-form.service';

describe('VehicleRequisition Form Service', () => {
  let service: VehicleRequisitionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleRequisitionFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleRequisitionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleRequisitionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purpose: expect.any(Object),
            otherPassengersName: expect.any(Object),
            totalNumberOfPassengers: expect.any(Object),
            status: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            transactionNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            area: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            approvedBy: expect.any(Object),
            requester: expect.any(Object),
            vehicle: expect.any(Object),
          })
        );
      });

      it('passing IVehicleRequisition should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleRequisitionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purpose: expect.any(Object),
            otherPassengersName: expect.any(Object),
            totalNumberOfPassengers: expect.any(Object),
            status: expect.any(Object),
            remarks: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            sanctionAt: expect.any(Object),
            transactionNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            area: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            approvedBy: expect.any(Object),
            requester: expect.any(Object),
            vehicle: expect.any(Object),
          })
        );
      });
    });

    describe('getVehicleRequisition', () => {
      it('should return NewVehicleRequisition for default VehicleRequisition initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVehicleRequisitionFormGroup(sampleWithNewData);

        const vehicleRequisition = service.getVehicleRequisition(formGroup) as any;

        expect(vehicleRequisition).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleRequisition for empty VehicleRequisition initial value', () => {
        const formGroup = service.createVehicleRequisitionFormGroup();

        const vehicleRequisition = service.getVehicleRequisition(formGroup) as any;

        expect(vehicleRequisition).toMatchObject({});
      });

      it('should return IVehicleRequisition', () => {
        const formGroup = service.createVehicleRequisitionFormGroup(sampleWithRequiredData);

        const vehicleRequisition = service.getVehicleRequisition(formGroup) as any;

        expect(vehicleRequisition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleRequisition should not enable id FormControl', () => {
        const formGroup = service.createVehicleRequisitionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleRequisition should disable id FormControl', () => {
        const formGroup = service.createVehicleRequisitionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
