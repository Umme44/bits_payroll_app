import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAttendanceSummary } from '../attendance-summary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../attendance-summary.test-samples';

import { AttendanceSummaryService, RestAttendanceSummary } from './attendance-summary.service';

const requireRestSample: RestAttendanceSummary = {
  ...sampleWithRequiredData,
  attendanceRegularisationStartDate: sampleWithRequiredData.attendanceRegularisationStartDate?.format(DATE_FORMAT),
  attendanceRegularisationEndDate: sampleWithRequiredData.attendanceRegularisationEndDate?.format(DATE_FORMAT),
};

describe('AttendanceSummary Service', () => {
  let service: AttendanceSummaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttendanceSummary | IAttendanceSummary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttendanceSummaryService);
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

    it('should create a AttendanceSummary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const attendanceSummary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(attendanceSummary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AttendanceSummary', () => {
      const attendanceSummary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(attendanceSummary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AttendanceSummary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttendanceSummary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AttendanceSummary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAttendanceSummaryToCollectionIfMissing', () => {
      it('should add a AttendanceSummary to an empty array', () => {
        const attendanceSummary: IAttendanceSummary = sampleWithRequiredData;
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing([], attendanceSummary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceSummary);
      });

      it('should not add a AttendanceSummary to an array that contains it', () => {
        const attendanceSummary: IAttendanceSummary = sampleWithRequiredData;
        const attendanceSummaryCollection: IAttendanceSummary[] = [
          {
            ...attendanceSummary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing(attendanceSummaryCollection, attendanceSummary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttendanceSummary to an array that doesn't contain it", () => {
        const attendanceSummary: IAttendanceSummary = sampleWithRequiredData;
        const attendanceSummaryCollection: IAttendanceSummary[] = [sampleWithPartialData];
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing(attendanceSummaryCollection, attendanceSummary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceSummary);
      });

      it('should add only unique AttendanceSummary to an array', () => {
        const attendanceSummaryArray: IAttendanceSummary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attendanceSummaryCollection: IAttendanceSummary[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing(attendanceSummaryCollection, ...attendanceSummaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attendanceSummary: IAttendanceSummary = sampleWithRequiredData;
        const attendanceSummary2: IAttendanceSummary = sampleWithPartialData;
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing([], attendanceSummary, attendanceSummary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendanceSummary);
        expect(expectedResult).toContain(attendanceSummary2);
      });

      it('should accept null and undefined values', () => {
        const attendanceSummary: IAttendanceSummary = sampleWithRequiredData;
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing([], null, attendanceSummary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendanceSummary);
      });

      it('should return initial array if no AttendanceSummary is added', () => {
        const attendanceSummaryCollection: IAttendanceSummary[] = [sampleWithRequiredData];
        expectedResult = service.addAttendanceSummaryToCollectionIfMissing(attendanceSummaryCollection, undefined, null);
        expect(expectedResult).toEqual(attendanceSummaryCollection);
      });
    });

    describe('compareAttendanceSummary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttendanceSummary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAttendanceSummary(entity1, entity2);
        const compareResult2 = service.compareAttendanceSummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAttendanceSummary(entity1, entity2);
        const compareResult2 = service.compareAttendanceSummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAttendanceSummary(entity1, entity2);
        const compareResult2 = service.compareAttendanceSummary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
