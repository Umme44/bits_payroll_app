import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hold-fb-disbursement.test-samples';

import { HoldFbDisbursementService, RestHoldFbDisbursement } from './hold-fb-disbursement.service';

const requireRestSample: RestHoldFbDisbursement = {
  ...sampleWithRequiredData,
  disbursedAt: sampleWithRequiredData.disbursedAt?.format(DATE_FORMAT),
};

describe('HoldFbDisbursement Service', () => {
  let service: HoldFbDisbursementService;
  let httpMock: HttpTestingController;
  let expectedResult: IHoldFbDisbursement | IHoldFbDisbursement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HoldFbDisbursementService);
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

    it('should create a HoldFbDisbursement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const holdFbDisbursement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(holdFbDisbursement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HoldFbDisbursement', () => {
      const holdFbDisbursement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(holdFbDisbursement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HoldFbDisbursement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HoldFbDisbursement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HoldFbDisbursement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHoldFbDisbursementToCollectionIfMissing', () => {
      it('should add a HoldFbDisbursement to an empty array', () => {
        const holdFbDisbursement: IHoldFbDisbursement = sampleWithRequiredData;
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing([], holdFbDisbursement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holdFbDisbursement);
      });

      it('should not add a HoldFbDisbursement to an array that contains it', () => {
        const holdFbDisbursement: IHoldFbDisbursement = sampleWithRequiredData;
        const holdFbDisbursementCollection: IHoldFbDisbursement[] = [
          {
            ...holdFbDisbursement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing(holdFbDisbursementCollection, holdFbDisbursement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HoldFbDisbursement to an array that doesn't contain it", () => {
        const holdFbDisbursement: IHoldFbDisbursement = sampleWithRequiredData;
        const holdFbDisbursementCollection: IHoldFbDisbursement[] = [sampleWithPartialData];
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing(holdFbDisbursementCollection, holdFbDisbursement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holdFbDisbursement);
      });

      it('should add only unique HoldFbDisbursement to an array', () => {
        const holdFbDisbursementArray: IHoldFbDisbursement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const holdFbDisbursementCollection: IHoldFbDisbursement[] = [sampleWithRequiredData];
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing(holdFbDisbursementCollection, ...holdFbDisbursementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const holdFbDisbursement: IHoldFbDisbursement = sampleWithRequiredData;
        const holdFbDisbursement2: IHoldFbDisbursement = sampleWithPartialData;
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing([], holdFbDisbursement, holdFbDisbursement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holdFbDisbursement);
        expect(expectedResult).toContain(holdFbDisbursement2);
      });

      it('should accept null and undefined values', () => {
        const holdFbDisbursement: IHoldFbDisbursement = sampleWithRequiredData;
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing([], null, holdFbDisbursement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holdFbDisbursement);
      });

      it('should return initial array if no HoldFbDisbursement is added', () => {
        const holdFbDisbursementCollection: IHoldFbDisbursement[] = [sampleWithRequiredData];
        expectedResult = service.addHoldFbDisbursementToCollectionIfMissing(holdFbDisbursementCollection, undefined, null);
        expect(expectedResult).toEqual(holdFbDisbursementCollection);
      });
    });

    describe('compareHoldFbDisbursement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHoldFbDisbursement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHoldFbDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldFbDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHoldFbDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldFbDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHoldFbDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldFbDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
