import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISalaryDeduction } from '../salary-deduction.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../salary-deduction.test-samples';

import { SalaryDeductionService } from './salary-deduction.service';

const requireRestSample: ISalaryDeduction = {
  ...sampleWithRequiredData,
};

describe('SalaryDeduction Service', () => {
  let service: SalaryDeductionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalaryDeduction | ISalaryDeduction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryDeductionService);
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

    it('should create a SalaryDeduction', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const salaryDeduction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salaryDeduction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalaryDeduction', () => {
      const salaryDeduction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salaryDeduction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalaryDeduction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalaryDeduction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalaryDeduction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalaryDeductionToCollectionIfMissing', () => {
      it('should add a SalaryDeduction to an empty array', () => {
        const salaryDeduction: ISalaryDeduction = sampleWithRequiredData;
        expectedResult = service.addSalaryDeductionToCollectionIfMissing([], salaryDeduction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryDeduction);
      });

      it('should not add a SalaryDeduction to an array that contains it', () => {
        const salaryDeduction: ISalaryDeduction = sampleWithRequiredData;
        const salaryDeductionCollection: ISalaryDeduction[] = [
          {
            ...salaryDeduction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryDeductionToCollectionIfMissing(salaryDeductionCollection, salaryDeduction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalaryDeduction to an array that doesn't contain it", () => {
        const salaryDeduction: ISalaryDeduction = sampleWithRequiredData;
        const salaryDeductionCollection: ISalaryDeduction[] = [sampleWithPartialData];
        expectedResult = service.addSalaryDeductionToCollectionIfMissing(salaryDeductionCollection, salaryDeduction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryDeduction);
      });

      it('should add only unique SalaryDeduction to an array', () => {
        const salaryDeductionArray: ISalaryDeduction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryDeductionCollection: ISalaryDeduction[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryDeductionToCollectionIfMissing(salaryDeductionCollection, ...salaryDeductionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salaryDeduction: ISalaryDeduction = sampleWithRequiredData;
        const salaryDeduction2: ISalaryDeduction = sampleWithPartialData;
        expectedResult = service.addSalaryDeductionToCollectionIfMissing([], salaryDeduction, salaryDeduction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryDeduction);
        expect(expectedResult).toContain(salaryDeduction2);
      });

      it('should accept null and undefined values', () => {
        const salaryDeduction: ISalaryDeduction = sampleWithRequiredData;
        expectedResult = service.addSalaryDeductionToCollectionIfMissing([], null, salaryDeduction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryDeduction);
      });

      it('should return initial array if no SalaryDeduction is added', () => {
        const salaryDeductionCollection: ISalaryDeduction[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryDeductionToCollectionIfMissing(salaryDeductionCollection, undefined, null);
        expect(expectedResult).toEqual(salaryDeductionCollection);
      });
    });

    describe('compareSalaryDeduction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalaryDeduction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalaryDeduction(entity1, entity2);
        const compareResult2 = service.compareSalaryDeduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalaryDeduction(entity1, entity2);
        const compareResult2 = service.compareSalaryDeduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalaryDeduction(entity1, entity2);
        const compareResult2 = service.compareSalaryDeduction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
