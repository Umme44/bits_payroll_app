import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IManualAttendanceEntry } from '../manual-attendance-entry.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../manual-attendance-entry.test-samples';

import { ManualAttendanceEntryService, RestManualAttendanceEntry } from './manual-attendance-entry.service';

const requireRestSample: RestManualAttendanceEntry = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  inTime: sampleWithRequiredData.inTime?.toJSON(),
  outTime: sampleWithRequiredData.outTime?.toJSON(),
};

describe('ManualAttendanceEntry Service', () => {
  let service: ManualAttendanceEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IManualAttendanceEntry | IManualAttendanceEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManualAttendanceEntryService);
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

    it('should create a ManualAttendanceEntry', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const manualAttendanceEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(manualAttendanceEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManualAttendanceEntry', () => {
      const manualAttendanceEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(manualAttendanceEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManualAttendanceEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManualAttendanceEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ManualAttendanceEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addManualAttendanceEntryToCollectionIfMissing', () => {
      it('should add a ManualAttendanceEntry to an empty array', () => {
        const manualAttendanceEntry: IManualAttendanceEntry = sampleWithRequiredData;
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing([], manualAttendanceEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualAttendanceEntry);
      });

      it('should not add a ManualAttendanceEntry to an array that contains it', () => {
        const manualAttendanceEntry: IManualAttendanceEntry = sampleWithRequiredData;
        const manualAttendanceEntryCollection: IManualAttendanceEntry[] = [
          {
            ...manualAttendanceEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing(manualAttendanceEntryCollection, manualAttendanceEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManualAttendanceEntry to an array that doesn't contain it", () => {
        const manualAttendanceEntry: IManualAttendanceEntry = sampleWithRequiredData;
        const manualAttendanceEntryCollection: IManualAttendanceEntry[] = [sampleWithPartialData];
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing(manualAttendanceEntryCollection, manualAttendanceEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualAttendanceEntry);
      });

      it('should add only unique ManualAttendanceEntry to an array', () => {
        const manualAttendanceEntryArray: IManualAttendanceEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const manualAttendanceEntryCollection: IManualAttendanceEntry[] = [sampleWithRequiredData];
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing(
          manualAttendanceEntryCollection,
          ...manualAttendanceEntryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manualAttendanceEntry: IManualAttendanceEntry = sampleWithRequiredData;
        const manualAttendanceEntry2: IManualAttendanceEntry = sampleWithPartialData;
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing([], manualAttendanceEntry, manualAttendanceEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualAttendanceEntry);
        expect(expectedResult).toContain(manualAttendanceEntry2);
      });

      it('should accept null and undefined values', () => {
        const manualAttendanceEntry: IManualAttendanceEntry = sampleWithRequiredData;
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing([], null, manualAttendanceEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualAttendanceEntry);
      });

      it('should return initial array if no ManualAttendanceEntry is added', () => {
        const manualAttendanceEntryCollection: IManualAttendanceEntry[] = [sampleWithRequiredData];
        expectedResult = service.addManualAttendanceEntryToCollectionIfMissing(manualAttendanceEntryCollection, undefined, null);
        expect(expectedResult).toEqual(manualAttendanceEntryCollection);
      });
    });

    describe('compareManualAttendanceEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareManualAttendanceEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareManualAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareManualAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareManualAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareManualAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareManualAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareManualAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
