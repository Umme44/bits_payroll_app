import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMobileBill } from '../mobile-bill.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../mobile-bill.test-samples';

import { MobileBillService } from './mobile-bill.service';

const requireRestSample: IMobileBill = {
  ...sampleWithRequiredData,
};

describe('MobileBill Service', () => {
  let service: MobileBillService;
  let httpMock: HttpTestingController;
  let expectedResult: IMobileBill | IMobileBill[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MobileBillService);
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

    it('should create a MobileBill', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const mobileBill = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mobileBill).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MobileBill', () => {
      const mobileBill = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mobileBill).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MobileBill', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MobileBill', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MobileBill', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMobileBillToCollectionIfMissing', () => {
      it('should add a MobileBill to an empty array', () => {
        const mobileBill: IMobileBill = sampleWithRequiredData;
        expectedResult = service.addMobileBillToCollectionIfMissing([], mobileBill);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mobileBill);
      });

      it('should not add a MobileBill to an array that contains it', () => {
        const mobileBill: IMobileBill = sampleWithRequiredData;
        const mobileBillCollection: IMobileBill[] = [
          {
            ...mobileBill,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMobileBillToCollectionIfMissing(mobileBillCollection, mobileBill);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MobileBill to an array that doesn't contain it", () => {
        const mobileBill: IMobileBill = sampleWithRequiredData;
        const mobileBillCollection: IMobileBill[] = [sampleWithPartialData];
        expectedResult = service.addMobileBillToCollectionIfMissing(mobileBillCollection, mobileBill);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mobileBill);
      });

      it('should add only unique MobileBill to an array', () => {
        const mobileBillArray: IMobileBill[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mobileBillCollection: IMobileBill[] = [sampleWithRequiredData];
        expectedResult = service.addMobileBillToCollectionIfMissing(mobileBillCollection, ...mobileBillArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mobileBill: IMobileBill = sampleWithRequiredData;
        const mobileBill2: IMobileBill = sampleWithPartialData;
        expectedResult = service.addMobileBillToCollectionIfMissing([], mobileBill, mobileBill2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mobileBill);
        expect(expectedResult).toContain(mobileBill2);
      });

      it('should accept null and undefined values', () => {
        const mobileBill: IMobileBill = sampleWithRequiredData;
        expectedResult = service.addMobileBillToCollectionIfMissing([], null, mobileBill, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mobileBill);
      });

      it('should return initial array if no MobileBill is added', () => {
        const mobileBillCollection: IMobileBill[] = [sampleWithRequiredData];
        expectedResult = service.addMobileBillToCollectionIfMissing(mobileBillCollection, undefined, null);
        expect(expectedResult).toEqual(mobileBillCollection);
      });
    });

    describe('compareMobileBill', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMobileBill(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMobileBill(entity1, entity2);
        const compareResult2 = service.compareMobileBill(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMobileBill(entity1, entity2);
        const compareResult2 = service.compareMobileBill(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMobileBill(entity1, entity2);
        const compareResult2 = service.compareMobileBill(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
