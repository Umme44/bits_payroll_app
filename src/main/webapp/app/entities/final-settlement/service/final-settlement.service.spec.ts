import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFinalSettlement } from '../final-settlement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../final-settlement.test-samples';

import { FinalSettlementService, RestFinalSettlement } from './final-settlement.service';

const requireRestSample: RestFinalSettlement = {
  ...sampleWithRequiredData,
  dateOfResignation: sampleWithRequiredData.dateOfResignation?.format(DATE_FORMAT),
  lastWorkingDay: sampleWithRequiredData.lastWorkingDay?.format(DATE_FORMAT),
  dateOfRelease: sampleWithRequiredData.dateOfRelease?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
  finalSettlementDate: sampleWithRequiredData.finalSettlementDate?.format(DATE_FORMAT),
};

describe('FinalSettlement Service', () => {
  let service: FinalSettlementService;
  let httpMock: HttpTestingController;
  let expectedResult: IFinalSettlement | IFinalSettlement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FinalSettlementService);
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

    it('should create a FinalSettlement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const finalSettlement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(finalSettlement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinalSettlement', () => {
      const finalSettlement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(finalSettlement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinalSettlement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinalSettlement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FinalSettlement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFinalSettlementToCollectionIfMissing', () => {
      it('should add a FinalSettlement to an empty array', () => {
        const finalSettlement: IFinalSettlement = sampleWithRequiredData;
        expectedResult = service.addFinalSettlementToCollectionIfMissing([], finalSettlement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finalSettlement);
      });

      it('should not add a FinalSettlement to an array that contains it', () => {
        const finalSettlement: IFinalSettlement = sampleWithRequiredData;
        const finalSettlementCollection: IFinalSettlement[] = [
          {
            ...finalSettlement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFinalSettlementToCollectionIfMissing(finalSettlementCollection, finalSettlement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinalSettlement to an array that doesn't contain it", () => {
        const finalSettlement: IFinalSettlement = sampleWithRequiredData;
        const finalSettlementCollection: IFinalSettlement[] = [sampleWithPartialData];
        expectedResult = service.addFinalSettlementToCollectionIfMissing(finalSettlementCollection, finalSettlement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finalSettlement);
      });

      it('should add only unique FinalSettlement to an array', () => {
        const finalSettlementArray: IFinalSettlement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const finalSettlementCollection: IFinalSettlement[] = [sampleWithRequiredData];
        expectedResult = service.addFinalSettlementToCollectionIfMissing(finalSettlementCollection, ...finalSettlementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const finalSettlement: IFinalSettlement = sampleWithRequiredData;
        const finalSettlement2: IFinalSettlement = sampleWithPartialData;
        expectedResult = service.addFinalSettlementToCollectionIfMissing([], finalSettlement, finalSettlement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finalSettlement);
        expect(expectedResult).toContain(finalSettlement2);
      });

      it('should accept null and undefined values', () => {
        const finalSettlement: IFinalSettlement = sampleWithRequiredData;
        expectedResult = service.addFinalSettlementToCollectionIfMissing([], null, finalSettlement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finalSettlement);
      });

      it('should return initial array if no FinalSettlement is added', () => {
        const finalSettlementCollection: IFinalSettlement[] = [sampleWithRequiredData];
        expectedResult = service.addFinalSettlementToCollectionIfMissing(finalSettlementCollection, undefined, null);
        expect(expectedResult).toEqual(finalSettlementCollection);
      });
    });

    describe('compareFinalSettlement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFinalSettlement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFinalSettlement(entity1, entity2);
        const compareResult2 = service.compareFinalSettlement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFinalSettlement(entity1, entity2);
        const compareResult2 = service.compareFinalSettlement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFinalSettlement(entity1, entity2);
        const compareResult2 = service.compareFinalSettlement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
