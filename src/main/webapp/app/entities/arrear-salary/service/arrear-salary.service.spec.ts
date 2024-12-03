import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArrearSalary } from '../arrear-salary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../arrear-salary.test-samples';

import { ArrearSalaryService } from './arrear-salary.service';

const requireRestSample: IArrearSalary = {
  ...sampleWithRequiredData,
};

describe('ArrearSalary Service', () => {
  let service: ArrearSalaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IArrearSalary | IArrearSalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArrearSalaryService);
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

    it('should create a ArrearSalary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const arrearSalary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(arrearSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArrearSalary', () => {
      const arrearSalary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(arrearSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArrearSalary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArrearSalary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArrearSalary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArrearSalaryToCollectionIfMissing', () => {
      it('should add a ArrearSalary to an empty array', () => {
        const arrearSalary: IArrearSalary = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryToCollectionIfMissing([], arrearSalary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalary);
      });

      it('should not add a ArrearSalary to an array that contains it', () => {
        const arrearSalary: IArrearSalary = sampleWithRequiredData;
        const arrearSalaryCollection: IArrearSalary[] = [
          {
            ...arrearSalary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArrearSalaryToCollectionIfMissing(arrearSalaryCollection, arrearSalary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArrearSalary to an array that doesn't contain it", () => {
        const arrearSalary: IArrearSalary = sampleWithRequiredData;
        const arrearSalaryCollection: IArrearSalary[] = [sampleWithPartialData];
        expectedResult = service.addArrearSalaryToCollectionIfMissing(arrearSalaryCollection, arrearSalary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalary);
      });

      it('should add only unique ArrearSalary to an array', () => {
        const arrearSalaryArray: IArrearSalary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const arrearSalaryCollection: IArrearSalary[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryToCollectionIfMissing(arrearSalaryCollection, ...arrearSalaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arrearSalary: IArrearSalary = sampleWithRequiredData;
        const arrearSalary2: IArrearSalary = sampleWithPartialData;
        expectedResult = service.addArrearSalaryToCollectionIfMissing([], arrearSalary, arrearSalary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalary);
        expect(expectedResult).toContain(arrearSalary2);
      });

      it('should accept null and undefined values', () => {
        const arrearSalary: IArrearSalary = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryToCollectionIfMissing([], null, arrearSalary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalary);
      });

      it('should return initial array if no ArrearSalary is added', () => {
        const arrearSalaryCollection: IArrearSalary[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryToCollectionIfMissing(arrearSalaryCollection, undefined, null);
        expect(expectedResult).toEqual(arrearSalaryCollection);
      });
    });

    describe('compareArrearSalary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArrearSalary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareArrearSalary(entity1, entity2);
        const compareResult2 = service.compareArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareArrearSalary(entity1, entity2);
        const compareResult2 = service.compareArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareArrearSalary(entity1, entity2);
        const compareResult2 = service.compareArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
