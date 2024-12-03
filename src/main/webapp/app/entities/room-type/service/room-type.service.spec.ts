import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoomType } from '../room-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../room-type.test-samples';

import { RoomTypeService, RestRoomType } from './room-type.service';

const requireRestSample: RestRoomType = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('RoomType Service', () => {
  let service: RoomTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoomType | IRoomType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoomTypeService);
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

    it('should create a RoomType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const roomType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(roomType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoomType', () => {
      const roomType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(roomType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RoomType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoomType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RoomType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRoomTypeToCollectionIfMissing', () => {
      it('should add a RoomType to an empty array', () => {
        const roomType: IRoomType = sampleWithRequiredData;
        expectedResult = service.addRoomTypeToCollectionIfMissing([], roomType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomType);
      });

      it('should not add a RoomType to an array that contains it', () => {
        const roomType: IRoomType = sampleWithRequiredData;
        const roomTypeCollection: IRoomType[] = [
          {
            ...roomType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoomTypeToCollectionIfMissing(roomTypeCollection, roomType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoomType to an array that doesn't contain it", () => {
        const roomType: IRoomType = sampleWithRequiredData;
        const roomTypeCollection: IRoomType[] = [sampleWithPartialData];
        expectedResult = service.addRoomTypeToCollectionIfMissing(roomTypeCollection, roomType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomType);
      });

      it('should add only unique RoomType to an array', () => {
        const roomTypeArray: IRoomType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roomTypeCollection: IRoomType[] = [sampleWithRequiredData];
        expectedResult = service.addRoomTypeToCollectionIfMissing(roomTypeCollection, ...roomTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roomType: IRoomType = sampleWithRequiredData;
        const roomType2: IRoomType = sampleWithPartialData;
        expectedResult = service.addRoomTypeToCollectionIfMissing([], roomType, roomType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomType);
        expect(expectedResult).toContain(roomType2);
      });

      it('should accept null and undefined values', () => {
        const roomType: IRoomType = sampleWithRequiredData;
        expectedResult = service.addRoomTypeToCollectionIfMissing([], null, roomType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomType);
      });

      it('should return initial array if no RoomType is added', () => {
        const roomTypeCollection: IRoomType[] = [sampleWithRequiredData];
        expectedResult = service.addRoomTypeToCollectionIfMissing(roomTypeCollection, undefined, null);
        expect(expectedResult).toEqual(roomTypeCollection);
      });
    });

    describe('compareRoomType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoomType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRoomType(entity1, entity2);
        const compareResult2 = service.compareRoomType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRoomType(entity1, entity2);
        const compareResult2 = service.compareRoomType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRoomType(entity1, entity2);
        const compareResult2 = service.compareRoomType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
