import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHolidays } from '../holidays.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../holidays.test-samples';

import { HolidaysService, RestHolidays } from './holidays.service';

const requireRestSample: RestHolidays = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Holidays Service', () => {
  let service: HolidaysService;
  let httpMock: HttpTestingController;
  let expectedResult: IHolidays | IHolidays[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HolidaysService);
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

    it('should create a Holidays', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const holidays = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(holidays).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Holidays', () => {
      const holidays = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(holidays).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Holidays', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Holidays', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Holidays', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHolidaysToCollectionIfMissing', () => {
      it('should add a Holidays to an empty array', () => {
        const holidays: IHolidays = sampleWithRequiredData;
        expectedResult = service.addHolidaysToCollectionIfMissing([], holidays);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holidays);
      });

      it('should not add a Holidays to an array that contains it', () => {
        const holidays: IHolidays = sampleWithRequiredData;
        const holidaysCollection: IHolidays[] = [
          {
            ...holidays,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHolidaysToCollectionIfMissing(holidaysCollection, holidays);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Holidays to an array that doesn't contain it", () => {
        const holidays: IHolidays = sampleWithRequiredData;
        const holidaysCollection: IHolidays[] = [sampleWithPartialData];
        expectedResult = service.addHolidaysToCollectionIfMissing(holidaysCollection, holidays);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holidays);
      });

      it('should add only unique Holidays to an array', () => {
        const holidaysArray: IHolidays[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const holidaysCollection: IHolidays[] = [sampleWithRequiredData];
        expectedResult = service.addHolidaysToCollectionIfMissing(holidaysCollection, ...holidaysArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const holidays: IHolidays = sampleWithRequiredData;
        const holidays2: IHolidays = sampleWithPartialData;
        expectedResult = service.addHolidaysToCollectionIfMissing([], holidays, holidays2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holidays);
        expect(expectedResult).toContain(holidays2);
      });

      it('should accept null and undefined values', () => {
        const holidays: IHolidays = sampleWithRequiredData;
        expectedResult = service.addHolidaysToCollectionIfMissing([], null, holidays, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holidays);
      });

      it('should return initial array if no Holidays is added', () => {
        const holidaysCollection: IHolidays[] = [sampleWithRequiredData];
        expectedResult = service.addHolidaysToCollectionIfMissing(holidaysCollection, undefined, null);
        expect(expectedResult).toEqual(holidaysCollection);
      });
    });

    describe('compareHolidays', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHolidays(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHolidays(entity1, entity2);
        const compareResult2 = service.compareHolidays(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHolidays(entity1, entity2);
        const compareResult2 = service.compareHolidays(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHolidays(entity1, entity2);
        const compareResult2 = service.compareHolidays(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
