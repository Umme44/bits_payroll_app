import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVehicleRequisition } from '../vehicle-requisition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../vehicle-requisition.test-samples';

import { VehicleRequisitionService, RestVehicleRequisition } from './vehicle-requisition.service';

const requireRestSample: RestVehicleRequisition = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  sanctionAt: sampleWithRequiredData.sanctionAt?.toJSON(),
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('VehicleRequisition Service', () => {
  let service: VehicleRequisitionService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicleRequisition | IVehicleRequisition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VehicleRequisition', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vehicleRequisition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicleRequisition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleRequisition', () => {
      const vehicleRequisition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicleRequisition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleRequisition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleRequisition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VehicleRequisition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicleRequisitionToCollectionIfMissing', () => {
      it('should add a VehicleRequisition to an empty array', () => {
        const vehicleRequisition: IVehicleRequisition = sampleWithRequiredData;
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing([], vehicleRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleRequisition);
      });

      it('should not add a VehicleRequisition to an array that contains it', () => {
        const vehicleRequisition: IVehicleRequisition = sampleWithRequiredData;
        const vehicleRequisitionCollection: IVehicleRequisition[] = [
          {
            ...vehicleRequisition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing(vehicleRequisitionCollection, vehicleRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleRequisition to an array that doesn't contain it", () => {
        const vehicleRequisition: IVehicleRequisition = sampleWithRequiredData;
        const vehicleRequisitionCollection: IVehicleRequisition[] = [sampleWithPartialData];
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing(vehicleRequisitionCollection, vehicleRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleRequisition);
      });

      it('should add only unique VehicleRequisition to an array', () => {
        const vehicleRequisitionArray: IVehicleRequisition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicleRequisitionCollection: IVehicleRequisition[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing(vehicleRequisitionCollection, ...vehicleRequisitionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleRequisition: IVehicleRequisition = sampleWithRequiredData;
        const vehicleRequisition2: IVehicleRequisition = sampleWithPartialData;
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing([], vehicleRequisition, vehicleRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleRequisition);
        expect(expectedResult).toContain(vehicleRequisition2);
      });

      it('should accept null and undefined values', () => {
        const vehicleRequisition: IVehicleRequisition = sampleWithRequiredData;
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing([], null, vehicleRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleRequisition);
      });

      it('should return initial array if no VehicleRequisition is added', () => {
        const vehicleRequisitionCollection: IVehicleRequisition[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleRequisitionToCollectionIfMissing(vehicleRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleRequisitionCollection);
      });
    });

    describe('compareVehicleRequisition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicleRequisition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVehicleRequisition(entity1, entity2);
        const compareResult2 = service.compareVehicleRequisition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVehicleRequisition(entity1, entity2);
        const compareResult2 = service.compareVehicleRequisition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVehicleRequisition(entity1, entity2);
        const compareResult2 = service.compareVehicleRequisition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
