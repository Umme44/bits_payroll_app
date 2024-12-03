import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../arrear-salary-item.test-samples';

import { ArrearSalaryItemService } from './arrear-salary-item.service';

const requireRestSample: IArrearSalaryItem = {
  ...sampleWithRequiredData,
};

describe('ArrearSalaryItem Service', () => {
  let service: ArrearSalaryItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IArrearSalaryItem | IArrearSalaryItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArrearSalaryItemService);
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

    it('should create a ArrearSalaryItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const arrearSalaryItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(arrearSalaryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArrearSalaryItem', () => {
      const arrearSalaryItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(arrearSalaryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArrearSalaryItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArrearSalaryItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArrearSalaryItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArrearSalaryItemToCollectionIfMissing', () => {
      it('should add a ArrearSalaryItem to an empty array', () => {
        const arrearSalaryItem: IArrearSalaryItem = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing([], arrearSalaryItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalaryItem);
      });

      it('should not add a ArrearSalaryItem to an array that contains it', () => {
        const arrearSalaryItem: IArrearSalaryItem = sampleWithRequiredData;
        const arrearSalaryItemCollection: IArrearSalaryItem[] = [
          {
            ...arrearSalaryItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing(arrearSalaryItemCollection, arrearSalaryItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArrearSalaryItem to an array that doesn't contain it", () => {
        const arrearSalaryItem: IArrearSalaryItem = sampleWithRequiredData;
        const arrearSalaryItemCollection: IArrearSalaryItem[] = [sampleWithPartialData];
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing(arrearSalaryItemCollection, arrearSalaryItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalaryItem);
      });

      it('should add only unique ArrearSalaryItem to an array', () => {
        const arrearSalaryItemArray: IArrearSalaryItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const arrearSalaryItemCollection: IArrearSalaryItem[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing(arrearSalaryItemCollection, ...arrearSalaryItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arrearSalaryItem: IArrearSalaryItem = sampleWithRequiredData;
        const arrearSalaryItem2: IArrearSalaryItem = sampleWithPartialData;
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing([], arrearSalaryItem, arrearSalaryItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalaryItem);
        expect(expectedResult).toContain(arrearSalaryItem2);
      });

      it('should accept null and undefined values', () => {
        const arrearSalaryItem: IArrearSalaryItem = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing([], null, arrearSalaryItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalaryItem);
      });

      it('should return initial array if no ArrearSalaryItem is added', () => {
        const arrearSalaryItemCollection: IArrearSalaryItem[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryItemToCollectionIfMissing(arrearSalaryItemCollection, undefined, null);
        expect(expectedResult).toEqual(arrearSalaryItemCollection);
      });
    });

    describe('compareArrearSalaryItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArrearSalaryItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareArrearSalaryItem(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareArrearSalaryItem(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareArrearSalaryItem(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
