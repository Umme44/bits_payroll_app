import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../unit-of-measurement.test-samples';

import { UnitOfMeasurementService, RestUnitOfMeasurement } from './unit-of-measurement.service';

const requireRestSample: RestUnitOfMeasurement = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('UnitOfMeasurement Service', () => {
  let service: UnitOfMeasurementService;
  let httpMock: HttpTestingController;
  let expectedResult: IUnitOfMeasurement | IUnitOfMeasurement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UnitOfMeasurementService);
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

    it('should create a UnitOfMeasurement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const unitOfMeasurement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(unitOfMeasurement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UnitOfMeasurement', () => {
      const unitOfMeasurement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(unitOfMeasurement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UnitOfMeasurement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UnitOfMeasurement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UnitOfMeasurement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUnitOfMeasurementToCollectionIfMissing', () => {
      it('should add a UnitOfMeasurement to an empty array', () => {
        const unitOfMeasurement: IUnitOfMeasurement = sampleWithRequiredData;
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing([], unitOfMeasurement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(unitOfMeasurement);
      });

      it('should not add a UnitOfMeasurement to an array that contains it', () => {
        const unitOfMeasurement: IUnitOfMeasurement = sampleWithRequiredData;
        const unitOfMeasurementCollection: IUnitOfMeasurement[] = [
          {
            ...unitOfMeasurement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing(unitOfMeasurementCollection, unitOfMeasurement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UnitOfMeasurement to an array that doesn't contain it", () => {
        const unitOfMeasurement: IUnitOfMeasurement = sampleWithRequiredData;
        const unitOfMeasurementCollection: IUnitOfMeasurement[] = [sampleWithPartialData];
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing(unitOfMeasurementCollection, unitOfMeasurement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(unitOfMeasurement);
      });

      it('should add only unique UnitOfMeasurement to an array', () => {
        const unitOfMeasurementArray: IUnitOfMeasurement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const unitOfMeasurementCollection: IUnitOfMeasurement[] = [sampleWithRequiredData];
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing(unitOfMeasurementCollection, ...unitOfMeasurementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const unitOfMeasurement: IUnitOfMeasurement = sampleWithRequiredData;
        const unitOfMeasurement2: IUnitOfMeasurement = sampleWithPartialData;
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing([], unitOfMeasurement, unitOfMeasurement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(unitOfMeasurement);
        expect(expectedResult).toContain(unitOfMeasurement2);
      });

      it('should accept null and undefined values', () => {
        const unitOfMeasurement: IUnitOfMeasurement = sampleWithRequiredData;
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing([], null, unitOfMeasurement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(unitOfMeasurement);
      });

      it('should return initial array if no UnitOfMeasurement is added', () => {
        const unitOfMeasurementCollection: IUnitOfMeasurement[] = [sampleWithRequiredData];
        expectedResult = service.addUnitOfMeasurementToCollectionIfMissing(unitOfMeasurementCollection, undefined, null);
        expect(expectedResult).toEqual(unitOfMeasurementCollection);
      });
    });

    describe('compareUnitOfMeasurement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUnitOfMeasurement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUnitOfMeasurement(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUnitOfMeasurement(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUnitOfMeasurement(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
