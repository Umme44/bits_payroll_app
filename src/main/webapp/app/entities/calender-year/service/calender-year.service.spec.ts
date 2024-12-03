import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICalenderYear } from '../calender-year.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../calender-year.test-samples';

import { CalenderYearService, RestCalenderYear } from './calender-year.service';

const requireRestSample: RestCalenderYear = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('CalenderYear Service', () => {
  let service: CalenderYearService;
  let httpMock: HttpTestingController;
  let expectedResult: ICalenderYear | ICalenderYear[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CalenderYearService);
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

    it('should create a CalenderYear', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const calenderYear = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(calenderYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CalenderYear', () => {
      const calenderYear = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(calenderYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CalenderYear', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CalenderYear', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CalenderYear', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCalenderYearToCollectionIfMissing', () => {
      it('should add a CalenderYear to an empty array', () => {
        const calenderYear: ICalenderYear = sampleWithRequiredData;
        expectedResult = service.addCalenderYearToCollectionIfMissing([], calenderYear);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(calenderYear);
      });

      it('should not add a CalenderYear to an array that contains it', () => {
        const calenderYear: ICalenderYear = sampleWithRequiredData;
        const calenderYearCollection: ICalenderYear[] = [
          {
            ...calenderYear,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCalenderYearToCollectionIfMissing(calenderYearCollection, calenderYear);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CalenderYear to an array that doesn't contain it", () => {
        const calenderYear: ICalenderYear = sampleWithRequiredData;
        const calenderYearCollection: ICalenderYear[] = [sampleWithPartialData];
        expectedResult = service.addCalenderYearToCollectionIfMissing(calenderYearCollection, calenderYear);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calenderYear);
      });

      it('should add only unique CalenderYear to an array', () => {
        const calenderYearArray: ICalenderYear[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const calenderYearCollection: ICalenderYear[] = [sampleWithRequiredData];
        expectedResult = service.addCalenderYearToCollectionIfMissing(calenderYearCollection, ...calenderYearArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const calenderYear: ICalenderYear = sampleWithRequiredData;
        const calenderYear2: ICalenderYear = sampleWithPartialData;
        expectedResult = service.addCalenderYearToCollectionIfMissing([], calenderYear, calenderYear2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calenderYear);
        expect(expectedResult).toContain(calenderYear2);
      });

      it('should accept null and undefined values', () => {
        const calenderYear: ICalenderYear = sampleWithRequiredData;
        expectedResult = service.addCalenderYearToCollectionIfMissing([], null, calenderYear, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(calenderYear);
      });

      it('should return initial array if no CalenderYear is added', () => {
        const calenderYearCollection: ICalenderYear[] = [sampleWithRequiredData];
        expectedResult = service.addCalenderYearToCollectionIfMissing(calenderYearCollection, undefined, null);
        expect(expectedResult).toEqual(calenderYearCollection);
      });
    });

    describe('compareCalenderYear', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCalenderYear(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCalenderYear(entity1, entity2);
        const compareResult2 = service.compareCalenderYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCalenderYear(entity1, entity2);
        const compareResult2 = service.compareCalenderYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCalenderYear(entity1, entity2);
        const compareResult2 = service.compareCalenderYear(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
