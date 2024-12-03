import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBuilding } from '../building.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../building.test-samples';

import { BuildingService, RestBuilding } from './building.service';

const requireRestSample: RestBuilding = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Building Service', () => {
  let service: BuildingService;
  let httpMock: HttpTestingController;
  let expectedResult: IBuilding | IBuilding[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BuildingService);
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

    it('should create a Building', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const building = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(building).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Building', () => {
      const building = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(building).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Building', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Building', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Building', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBuildingToCollectionIfMissing', () => {
      it('should add a Building to an empty array', () => {
        const building: IBuilding = sampleWithRequiredData;
        expectedResult = service.addBuildingToCollectionIfMissing([], building);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(building);
      });

      it('should not add a Building to an array that contains it', () => {
        const building: IBuilding = sampleWithRequiredData;
        const buildingCollection: IBuilding[] = [
          {
            ...building,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBuildingToCollectionIfMissing(buildingCollection, building);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Building to an array that doesn't contain it", () => {
        const building: IBuilding = sampleWithRequiredData;
        const buildingCollection: IBuilding[] = [sampleWithPartialData];
        expectedResult = service.addBuildingToCollectionIfMissing(buildingCollection, building);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(building);
      });

      it('should add only unique Building to an array', () => {
        const buildingArray: IBuilding[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const buildingCollection: IBuilding[] = [sampleWithRequiredData];
        expectedResult = service.addBuildingToCollectionIfMissing(buildingCollection, ...buildingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const building: IBuilding = sampleWithRequiredData;
        const building2: IBuilding = sampleWithPartialData;
        expectedResult = service.addBuildingToCollectionIfMissing([], building, building2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(building);
        expect(expectedResult).toContain(building2);
      });

      it('should accept null and undefined values', () => {
        const building: IBuilding = sampleWithRequiredData;
        expectedResult = service.addBuildingToCollectionIfMissing([], null, building, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(building);
      });

      it('should return initial array if no Building is added', () => {
        const buildingCollection: IBuilding[] = [sampleWithRequiredData];
        expectedResult = service.addBuildingToCollectionIfMissing(buildingCollection, undefined, null);
        expect(expectedResult).toEqual(buildingCollection);
      });
    });

    describe('compareBuilding', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBuilding(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBuilding(entity1, entity2);
        const compareResult2 = service.compareBuilding(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBuilding(entity1, entity2);
        const compareResult2 = service.compareBuilding(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBuilding(entity1, entity2);
        const compareResult2 = service.compareBuilding(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
