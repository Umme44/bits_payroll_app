import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMovementEntry } from '../movement-entry.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../movement-entry.test-samples';

import { MovementEntryService, RestMovementEntry } from './movement-entry.service';

const requireRestSample: RestMovementEntry = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
  sanctionAt: sampleWithRequiredData.sanctionAt?.format(DATE_FORMAT),
};

describe('MovementEntry Service', () => {
  let service: MovementEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMovementEntry | IMovementEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MovementEntryService);
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

    it('should create a MovementEntry', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const movementEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(movementEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MovementEntry', () => {
      const movementEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(movementEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MovementEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MovementEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MovementEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMovementEntryToCollectionIfMissing', () => {
      it('should add a MovementEntry to an empty array', () => {
        const movementEntry: IMovementEntry = sampleWithRequiredData;
        expectedResult = service.addMovementEntryToCollectionIfMissing([], movementEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movementEntry);
      });

      it('should not add a MovementEntry to an array that contains it', () => {
        const movementEntry: IMovementEntry = sampleWithRequiredData;
        const movementEntryCollection: IMovementEntry[] = [
          {
            ...movementEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMovementEntryToCollectionIfMissing(movementEntryCollection, movementEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MovementEntry to an array that doesn't contain it", () => {
        const movementEntry: IMovementEntry = sampleWithRequiredData;
        const movementEntryCollection: IMovementEntry[] = [sampleWithPartialData];
        expectedResult = service.addMovementEntryToCollectionIfMissing(movementEntryCollection, movementEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movementEntry);
      });

      it('should add only unique MovementEntry to an array', () => {
        const movementEntryArray: IMovementEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const movementEntryCollection: IMovementEntry[] = [sampleWithRequiredData];
        expectedResult = service.addMovementEntryToCollectionIfMissing(movementEntryCollection, ...movementEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const movementEntry: IMovementEntry = sampleWithRequiredData;
        const movementEntry2: IMovementEntry = sampleWithPartialData;
        expectedResult = service.addMovementEntryToCollectionIfMissing([], movementEntry, movementEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movementEntry);
        expect(expectedResult).toContain(movementEntry2);
      });

      it('should accept null and undefined values', () => {
        const movementEntry: IMovementEntry = sampleWithRequiredData;
        expectedResult = service.addMovementEntryToCollectionIfMissing([], null, movementEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movementEntry);
      });

      it('should return initial array if no MovementEntry is added', () => {
        const movementEntryCollection: IMovementEntry[] = [sampleWithRequiredData];
        expectedResult = service.addMovementEntryToCollectionIfMissing(movementEntryCollection, undefined, null);
        expect(expectedResult).toEqual(movementEntryCollection);
      });
    });

    describe('compareMovementEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMovementEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMovementEntry(entity1, entity2);
        const compareResult2 = service.compareMovementEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMovementEntry(entity1, entity2);
        const compareResult2 = service.compareMovementEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMovementEntry(entity1, entity2);
        const compareResult2 = service.compareMovementEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
