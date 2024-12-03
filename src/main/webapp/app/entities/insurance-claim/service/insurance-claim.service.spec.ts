import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInsuranceClaim } from '../insurance-claim.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../insurance-claim.test-samples';

import { InsuranceClaimService, RestInsuranceClaim } from './insurance-claim.service';

const requireRestSample: RestInsuranceClaim = {
  ...sampleWithRequiredData,
  settlementDate: sampleWithRequiredData.settlementDate?.format(DATE_FORMAT),
  paymentDate: sampleWithRequiredData.paymentDate?.format(DATE_FORMAT),
  regretDate: sampleWithRequiredData.regretDate?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('InsuranceClaim Service', () => {
  let service: InsuranceClaimService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsuranceClaim | IInsuranceClaim[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InsuranceClaimService);
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

    it('should create a InsuranceClaim', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const insuranceClaim = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insuranceClaim).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsuranceClaim', () => {
      const insuranceClaim = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insuranceClaim).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsuranceClaim', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsuranceClaim', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsuranceClaim', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInsuranceClaimToCollectionIfMissing', () => {
      it('should add a InsuranceClaim to an empty array', () => {
        const insuranceClaim: IInsuranceClaim = sampleWithRequiredData;
        expectedResult = service.addInsuranceClaimToCollectionIfMissing([], insuranceClaim);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceClaim);
      });

      it('should not add a InsuranceClaim to an array that contains it', () => {
        const insuranceClaim: IInsuranceClaim = sampleWithRequiredData;
        const insuranceClaimCollection: IInsuranceClaim[] = [
          {
            ...insuranceClaim,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsuranceClaimToCollectionIfMissing(insuranceClaimCollection, insuranceClaim);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsuranceClaim to an array that doesn't contain it", () => {
        const insuranceClaim: IInsuranceClaim = sampleWithRequiredData;
        const insuranceClaimCollection: IInsuranceClaim[] = [sampleWithPartialData];
        expectedResult = service.addInsuranceClaimToCollectionIfMissing(insuranceClaimCollection, insuranceClaim);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceClaim);
      });

      it('should add only unique InsuranceClaim to an array', () => {
        const insuranceClaimArray: IInsuranceClaim[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insuranceClaimCollection: IInsuranceClaim[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceClaimToCollectionIfMissing(insuranceClaimCollection, ...insuranceClaimArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insuranceClaim: IInsuranceClaim = sampleWithRequiredData;
        const insuranceClaim2: IInsuranceClaim = sampleWithPartialData;
        expectedResult = service.addInsuranceClaimToCollectionIfMissing([], insuranceClaim, insuranceClaim2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceClaim);
        expect(expectedResult).toContain(insuranceClaim2);
      });

      it('should accept null and undefined values', () => {
        const insuranceClaim: IInsuranceClaim = sampleWithRequiredData;
        expectedResult = service.addInsuranceClaimToCollectionIfMissing([], null, insuranceClaim, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceClaim);
      });

      it('should return initial array if no InsuranceClaim is added', () => {
        const insuranceClaimCollection: IInsuranceClaim[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceClaimToCollectionIfMissing(insuranceClaimCollection, undefined, null);
        expect(expectedResult).toEqual(insuranceClaimCollection);
      });
    });

    describe('compareInsuranceClaim', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsuranceClaim(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInsuranceClaim(entity1, entity2);
        const compareResult2 = service.compareInsuranceClaim(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInsuranceClaim(entity1, entity2);
        const compareResult2 = service.compareInsuranceClaim(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInsuranceClaim(entity1, entity2);
        const compareResult2 = service.compareInsuranceClaim(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
