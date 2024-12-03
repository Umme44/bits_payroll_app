import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmploymentHistory } from '../employment-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employment-history.test-samples';

import { EmploymentHistoryService, RestEmploymentHistory } from './employment-history.service';

const requireRestSample: RestEmploymentHistory = {
  ...sampleWithRequiredData,
  effectiveDate: sampleWithRequiredData.effectiveDate?.format(DATE_FORMAT),
};

describe('EmploymentHistory Service', () => {
  let service: EmploymentHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmploymentHistory | IEmploymentHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmploymentHistoryService);
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

    it('should create a EmploymentHistory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employmentHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employmentHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmploymentHistory', () => {
      const employmentHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employmentHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmploymentHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmploymentHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmploymentHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmploymentHistoryToCollectionIfMissing', () => {
      it('should add a EmploymentHistory to an empty array', () => {
        const employmentHistory: IEmploymentHistory = sampleWithRequiredData;
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing([], employmentHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentHistory);
      });

      it('should not add a EmploymentHistory to an array that contains it', () => {
        const employmentHistory: IEmploymentHistory = sampleWithRequiredData;
        const employmentHistoryCollection: IEmploymentHistory[] = [
          {
            ...employmentHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing(employmentHistoryCollection, employmentHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmploymentHistory to an array that doesn't contain it", () => {
        const employmentHistory: IEmploymentHistory = sampleWithRequiredData;
        const employmentHistoryCollection: IEmploymentHistory[] = [sampleWithPartialData];
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing(employmentHistoryCollection, employmentHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentHistory);
      });

      it('should add only unique EmploymentHistory to an array', () => {
        const employmentHistoryArray: IEmploymentHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employmentHistoryCollection: IEmploymentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing(employmentHistoryCollection, ...employmentHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employmentHistory: IEmploymentHistory = sampleWithRequiredData;
        const employmentHistory2: IEmploymentHistory = sampleWithPartialData;
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing([], employmentHistory, employmentHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentHistory);
        expect(expectedResult).toContain(employmentHistory2);
      });

      it('should accept null and undefined values', () => {
        const employmentHistory: IEmploymentHistory = sampleWithRequiredData;
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing([], null, employmentHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentHistory);
      });

      it('should return initial array if no EmploymentHistory is added', () => {
        const employmentHistoryCollection: IEmploymentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentHistoryToCollectionIfMissing(employmentHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(employmentHistoryCollection);
      });
    });

    describe('compareEmploymentHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmploymentHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmploymentHistory(entity1, entity2);
        const compareResult2 = service.compareEmploymentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmploymentHistory(entity1, entity2);
        const compareResult2 = service.compareEmploymentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmploymentHistory(entity1, entity2);
        const compareResult2 = service.compareEmploymentHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
