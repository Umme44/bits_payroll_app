import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttendanceSyncCache } from '../attendance-sync-cache.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../attendance-sync-cache.test-samples';

import { AttendanceSyncCacheService, RestAttendanceSyncCache } from './attendance-sync-cache.service';

const requireRestSample: RestAttendanceSyncCache = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('AttendanceSyncCache Service', () => {
  let service: AttendanceSyncCacheService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttendanceSyncCache | IAttendanceSyncCache[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttendanceSyncCacheService);
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

    it('should create a AttendanceSyncCache', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const attendanceSyncCache = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(attendanceSyncCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AttendanceSyncCache', () => {
      const attendanceSyncCache = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(attendanceSyncCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AttendanceSyncCache', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttendanceSyncCache', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AttendanceSyncCache', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAttendanceSyncCacheToCollectionIfMissing', () => {
      it('should add a AttendanceSyncCache to an empty array', () => {
        const attendanceSyncCache: IAttendanceSyncCache = sampleWithRequiredData;
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing([], attendanceSyncCache);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceSyncCache);
      });

      it('should not add a AttendanceSyncCache to an array that contains it', () => {
        const attendanceSyncCache: IAttendanceSyncCache = sampleWithRequiredData;
        const attendanceSyncCacheCollection: IAttendanceSyncCache[] = [
          {
            ...attendanceSyncCache,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing(attendanceSyncCacheCollection, attendanceSyncCache);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttendanceSyncCache to an array that doesn't contain it", () => {
        const attendanceSyncCache: IAttendanceSyncCache = sampleWithRequiredData;
        const attendanceSyncCacheCollection: IAttendanceSyncCache[] = [sampleWithPartialData];
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing(attendanceSyncCacheCollection, attendanceSyncCache);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceSyncCache);
      });

      it('should add only unique AttendanceSyncCache to an array', () => {
        const attendanceSyncCacheArray: IAttendanceSyncCache[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attendanceSyncCacheCollection: IAttendanceSyncCache[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing(attendanceSyncCacheCollection, ...attendanceSyncCacheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attendanceSyncCache: IAttendanceSyncCache = sampleWithRequiredData;
        const attendanceSyncCache2: IAttendanceSyncCache = sampleWithPartialData;
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing([], attendanceSyncCache, attendanceSyncCache2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceSyncCache);
        expect(expectedResult).toContain(attendanceSyncCache2);
      });

      it('should accept null and undefined values', () => {
        const attendanceSyncCache: IAttendanceSyncCache = sampleWithRequiredData;
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing([], null, attendanceSyncCache, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceSyncCache);
      });

      it('should return initial array if no AttendanceSyncCache is added', () => {
        const attendanceSyncCacheCollection: IAttendanceSyncCache[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceSyncCacheToCollectionIfMissing(attendanceSyncCacheCollection, undefined, null);
        expect(expectedResult).toEqual(attendanceSyncCacheCollection);
      });
    });

    describe('compareAttendanceSyncCache', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttendanceSyncCache(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAttendanceSyncCache(entity1, entity2);
        const compareResult2 = service.compareAttendanceSyncCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAttendanceSyncCache(entity1, entity2);
        const compareResult2 = service.compareAttendanceSyncCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAttendanceSyncCache(entity1, entity2);
        const compareResult2 = service.compareAttendanceSyncCache(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
