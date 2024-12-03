import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../individual-arrear-salary.test-samples';

import { IndividualArrearSalaryService, RestIndividualArrearSalary } from './individual-arrear-salary.service';

const requireRestSample: RestIndividualArrearSalary = {
  ...sampleWithRequiredData,
  effectiveDate: sampleWithRequiredData.effectiveDate?.format(DATE_FORMAT),
};

describe('IndividualArrearSalary Service', () => {
  let service: IndividualArrearSalaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IIndividualArrearSalary | IIndividualArrearSalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IndividualArrearSalaryService);
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

    it('should create a IndividualArrearSalary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const individualArrearSalary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(individualArrearSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IndividualArrearSalary', () => {
      const individualArrearSalary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(individualArrearSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IndividualArrearSalary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IndividualArrearSalary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IndividualArrearSalary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIndividualArrearSalaryToCollectionIfMissing', () => {
      it('should add a IndividualArrearSalary to an empty array', () => {
        const individualArrearSalary: IIndividualArrearSalary = sampleWithRequiredData;
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing([], individualArrearSalary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(individualArrearSalary);
      });

      it('should not add a IndividualArrearSalary to an array that contains it', () => {
        const individualArrearSalary: IIndividualArrearSalary = sampleWithRequiredData;
        const individualArrearSalaryCollection: IIndividualArrearSalary[] = [
          {
            ...individualArrearSalary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing(individualArrearSalaryCollection, individualArrearSalary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IndividualArrearSalary to an array that doesn't contain it", () => {
        const individualArrearSalary: IIndividualArrearSalary = sampleWithRequiredData;
        const individualArrearSalaryCollection: IIndividualArrearSalary[] = [sampleWithPartialData];
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing(individualArrearSalaryCollection, individualArrearSalary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(individualArrearSalary);
      });

      it('should add only unique IndividualArrearSalary to an array', () => {
        const individualArrearSalaryArray: IIndividualArrearSalary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const individualArrearSalaryCollection: IIndividualArrearSalary[] = [sampleWithRequiredData];
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing(
          individualArrearSalaryCollection,
          ...individualArrearSalaryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const individualArrearSalary: IIndividualArrearSalary = sampleWithRequiredData;
        const individualArrearSalary2: IIndividualArrearSalary = sampleWithPartialData;
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing([], individualArrearSalary, individualArrearSalary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(individualArrearSalary);
        expect(expectedResult).toContain(individualArrearSalary2);
      });

      it('should accept null and undefined values', () => {
        const individualArrearSalary: IIndividualArrearSalary = sampleWithRequiredData;
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing([], null, individualArrearSalary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(individualArrearSalary);
      });

      it('should return initial array if no IndividualArrearSalary is added', () => {
        const individualArrearSalaryCollection: IIndividualArrearSalary[] = [sampleWithRequiredData];
        expectedResult = service.addIndividualArrearSalaryToCollectionIfMissing(individualArrearSalaryCollection, undefined, null);
        expect(expectedResult).toEqual(individualArrearSalaryCollection);
      });
    });

    describe('compareIndividualArrearSalary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIndividualArrearSalary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIndividualArrearSalary(entity1, entity2);
        const compareResult2 = service.compareIndividualArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIndividualArrearSalary(entity1, entity2);
        const compareResult2 = service.compareIndividualArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIndividualArrearSalary(entity1, entity2);
        const compareResult2 = service.compareIndividualArrearSalary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
