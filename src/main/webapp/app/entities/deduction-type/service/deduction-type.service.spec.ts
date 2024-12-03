import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeductionType } from '../deduction-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../deduction-type.test-samples';

import { DeductionTypeService } from './deduction-type.service';

const requireRestSample: IDeductionType = {
  ...sampleWithRequiredData,
};

describe('DeductionType Service', () => {
  let service: DeductionTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeductionType | IDeductionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeductionTypeService);
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

    it('should create a DeductionType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deductionType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deductionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeductionType', () => {
      const deductionType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deductionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeductionType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeductionType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeductionType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeductionTypeToCollectionIfMissing', () => {
      it('should add a DeductionType to an empty array', () => {
        const deductionType: IDeductionType = sampleWithRequiredData;
        expectedResult = service.addDeductionTypeToCollectionIfMissing([], deductionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deductionType);
      });

      it('should not add a DeductionType to an array that contains it', () => {
        const deductionType: IDeductionType = sampleWithRequiredData;
        const deductionTypeCollection: IDeductionType[] = [
          {
            ...deductionType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeductionTypeToCollectionIfMissing(deductionTypeCollection, deductionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeductionType to an array that doesn't contain it", () => {
        const deductionType: IDeductionType = sampleWithRequiredData;
        const deductionTypeCollection: IDeductionType[] = [sampleWithPartialData];
        expectedResult = service.addDeductionTypeToCollectionIfMissing(deductionTypeCollection, deductionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deductionType);
      });

      it('should add only unique DeductionType to an array', () => {
        const deductionTypeArray: IDeductionType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deductionTypeCollection: IDeductionType[] = [sampleWithRequiredData];
        expectedResult = service.addDeductionTypeToCollectionIfMissing(deductionTypeCollection, ...deductionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deductionType: IDeductionType = sampleWithRequiredData;
        const deductionType2: IDeductionType = sampleWithPartialData;
        expectedResult = service.addDeductionTypeToCollectionIfMissing([], deductionType, deductionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deductionType);
        expect(expectedResult).toContain(deductionType2);
      });

      it('should accept null and undefined values', () => {
        const deductionType: IDeductionType = sampleWithRequiredData;
        expectedResult = service.addDeductionTypeToCollectionIfMissing([], null, deductionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deductionType);
      });

      it('should return initial array if no DeductionType is added', () => {
        const deductionTypeCollection: IDeductionType[] = [sampleWithRequiredData];
        expectedResult = service.addDeductionTypeToCollectionIfMissing(deductionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(deductionTypeCollection);
      });
    });

    describe('compareDeductionType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeductionType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeductionType(entity1, entity2);
        const compareResult2 = service.compareDeductionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeductionType(entity1, entity2);
        const compareResult2 = service.compareDeductionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeductionType(entity1, entity2);
        const compareResult2 = service.compareDeductionType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
