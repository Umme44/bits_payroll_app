import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../hold-salary-disbursement.test-samples';

import { HoldSalaryDisbursementService, RestHoldSalaryDisbursement } from './hold-salary-disbursement.service';

const requireRestSample: RestHoldSalaryDisbursement = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('HoldSalaryDisbursement Service', () => {
  let service: HoldSalaryDisbursementService;
  let httpMock: HttpTestingController;
  let expectedResult: IHoldSalaryDisbursement | IHoldSalaryDisbursement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HoldSalaryDisbursementService);
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

    it('should create a HoldSalaryDisbursement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const holdSalaryDisbursement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(holdSalaryDisbursement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HoldSalaryDisbursement', () => {
      const holdSalaryDisbursement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(holdSalaryDisbursement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HoldSalaryDisbursement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HoldSalaryDisbursement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HoldSalaryDisbursement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHoldSalaryDisbursementToCollectionIfMissing', () => {
      it('should add a HoldSalaryDisbursement to an empty array', () => {
        const holdSalaryDisbursement: IHoldSalaryDisbursement = sampleWithRequiredData;
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing([], holdSalaryDisbursement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holdSalaryDisbursement);
      });

      it('should not add a HoldSalaryDisbursement to an array that contains it', () => {
        const holdSalaryDisbursement: IHoldSalaryDisbursement = sampleWithRequiredData;
        const holdSalaryDisbursementCollection: IHoldSalaryDisbursement[] = [
          {
            ...holdSalaryDisbursement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing(holdSalaryDisbursementCollection, holdSalaryDisbursement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HoldSalaryDisbursement to an array that doesn't contain it", () => {
        const holdSalaryDisbursement: IHoldSalaryDisbursement = sampleWithRequiredData;
        const holdSalaryDisbursementCollection: IHoldSalaryDisbursement[] = [sampleWithPartialData];
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing(holdSalaryDisbursementCollection, holdSalaryDisbursement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holdSalaryDisbursement);
      });

      it('should add only unique HoldSalaryDisbursement to an array', () => {
        const holdSalaryDisbursementArray: IHoldSalaryDisbursement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const holdSalaryDisbursementCollection: IHoldSalaryDisbursement[] = [sampleWithRequiredData];
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing(
          holdSalaryDisbursementCollection,
          ...holdSalaryDisbursementArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const holdSalaryDisbursement: IHoldSalaryDisbursement = sampleWithRequiredData;
        const holdSalaryDisbursement2: IHoldSalaryDisbursement = sampleWithPartialData;
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing([], holdSalaryDisbursement, holdSalaryDisbursement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holdSalaryDisbursement);
        expect(expectedResult).toContain(holdSalaryDisbursement2);
      });

      it('should accept null and undefined values', () => {
        const holdSalaryDisbursement: IHoldSalaryDisbursement = sampleWithRequiredData;
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing([], null, holdSalaryDisbursement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holdSalaryDisbursement);
      });

      it('should return initial array if no HoldSalaryDisbursement is added', () => {
        const holdSalaryDisbursementCollection: IHoldSalaryDisbursement[] = [sampleWithRequiredData];
        expectedResult = service.addHoldSalaryDisbursementToCollectionIfMissing(holdSalaryDisbursementCollection, undefined, null);
        expect(expectedResult).toEqual(holdSalaryDisbursementCollection);
      });
    });

    describe('compareHoldSalaryDisbursement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHoldSalaryDisbursement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHoldSalaryDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldSalaryDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHoldSalaryDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldSalaryDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHoldSalaryDisbursement(entity1, entity2);
        const compareResult2 = service.compareHoldSalaryDisbursement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
