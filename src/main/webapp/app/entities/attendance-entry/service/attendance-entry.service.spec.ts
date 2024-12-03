import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAttendanceEntry } from '../attendance-entry.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../attendance-entry.test-samples';

import { AttendanceEntryService, RestAttendanceEntry } from './attendance-entry.service';

const requireRestSample: RestAttendanceEntry = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  inTime: sampleWithRequiredData.inTime?.toJSON(),
  outTime: sampleWithRequiredData.outTime?.toJSON(),
};

describe('AttendanceEntry Service', () => {
  let service: AttendanceEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttendanceEntry | IAttendanceEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttendanceEntryService);
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

    it('should create a AttendanceEntry', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const attendanceEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(attendanceEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AttendanceEntry', () => {
      const attendanceEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(attendanceEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AttendanceEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttendanceEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AttendanceEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAttendanceEntryToCollectionIfMissing', () => {
      it('should add a AttendanceEntry to an empty array', () => {
        const attendanceEntry: IAttendanceEntry = sampleWithRequiredData;
        expectedResult = service.addAttendanceEntryToCollectionIfMissing([], attendanceEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceEntry);
      });

      it('should not add a AttendanceEntry to an array that contains it', () => {
        const attendanceEntry: IAttendanceEntry = sampleWithRequiredData;
        const attendanceEntryCollection: IAttendanceEntry[] = [
          {
            ...attendanceEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttendanceEntryToCollectionIfMissing(attendanceEntryCollection, attendanceEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttendanceEntry to an array that doesn't contain it", () => {
        const attendanceEntry: IAttendanceEntry = sampleWithRequiredData;
        const attendanceEntryCollection: IAttendanceEntry[] = [sampleWithPartialData];
        expectedResult = service.addAttendanceEntryToCollectionIfMissing(attendanceEntryCollection, attendanceEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceEntry);
      });

      it('should add only unique AttendanceEntry to an array', () => {
        const attendanceEntryArray: IAttendanceEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attendanceEntryCollection: IAttendanceEntry[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceEntryToCollectionIfMissing(attendanceEntryCollection, ...attendanceEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attendanceEntry: IAttendanceEntry = sampleWithRequiredData;
        const attendanceEntry2: IAttendanceEntry = sampleWithPartialData;
        expectedResult = service.addAttendanceEntryToCollectionIfMissing([], attendanceEntry, attendanceEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceEntry);
        expect(expectedResult).toContain(attendanceEntry2);
      });

      it('should accept null and undefined values', () => {
        const attendanceEntry: IAttendanceEntry = sampleWithRequiredData;
        expectedResult = service.addAttendanceEntryToCollectionIfMissing([], null, attendanceEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceEntry);
      });

      it('should return initial array if no AttendanceEntry is added', () => {
        const attendanceEntryCollection: IAttendanceEntry[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceEntryToCollectionIfMissing(attendanceEntryCollection, undefined, null);
        expect(expectedResult).toEqual(attendanceEntryCollection);
      });
    });

    describe('compareAttendanceEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttendanceEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAttendanceEntry(entity1, entity2);
        const compareResult2 = service.compareAttendanceEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
