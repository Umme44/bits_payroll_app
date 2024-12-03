import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFloor } from '../floor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../floor.test-samples';

import { FloorService, RestFloor } from './floor.service';

const requireRestSample: RestFloor = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Floor Service', () => {
  let service: FloorService;
  let httpMock: HttpTestingController;
  let expectedResult: IFloor | IFloor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FloorService);
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

    it('should create a Floor', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const floor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(floor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Floor', () => {
      const floor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(floor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Floor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Floor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Floor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFloorToCollectionIfMissing', () => {
      it('should add a Floor to an empty array', () => {
        const floor: IFloor = sampleWithRequiredData;
        expectedResult = service.addFloorToCollectionIfMissing([], floor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(floor);
      });

      it('should not add a Floor to an array that contains it', () => {
        const floor: IFloor = sampleWithRequiredData;
        const floorCollection: IFloor[] = [
          {
            ...floor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFloorToCollectionIfMissing(floorCollection, floor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Floor to an array that doesn't contain it", () => {
        const floor: IFloor = sampleWithRequiredData;
        const floorCollection: IFloor[] = [sampleWithPartialData];
        expectedResult = service.addFloorToCollectionIfMissing(floorCollection, floor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(floor);
      });

      it('should add only unique Floor to an array', () => {
        const floorArray: IFloor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const floorCollection: IFloor[] = [sampleWithRequiredData];
        expectedResult = service.addFloorToCollectionIfMissing(floorCollection, ...floorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const floor: IFloor = sampleWithRequiredData;
        const floor2: IFloor = sampleWithPartialData;
        expectedResult = service.addFloorToCollectionIfMissing([], floor, floor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(floor);
        expect(expectedResult).toContain(floor2);
      });

      it('should accept null and undefined values', () => {
        const floor: IFloor = sampleWithRequiredData;
        expectedResult = service.addFloorToCollectionIfMissing([], null, floor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(floor);
      });

      it('should return initial array if no Floor is added', () => {
        const floorCollection: IFloor[] = [sampleWithRequiredData];
        expectedResult = service.addFloorToCollectionIfMissing(floorCollection, undefined, null);
        expect(expectedResult).toEqual(floorCollection);
      });
    });

    describe('compareFloor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFloor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFloor(entity1, entity2);
        const compareResult2 = service.compareFloor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFloor(entity1, entity2);
        const compareResult2 = service.compareFloor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFloor(entity1, entity2);
        const compareResult2 = service.compareFloor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
