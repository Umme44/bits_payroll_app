import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPfAccount } from '../pf-account.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-account.test-samples';

import { PfAccountService, RestPfAccount } from './pf-account.service';

const requireRestSample: RestPfAccount = {
  ...sampleWithRequiredData,
  membershipStartDate: sampleWithRequiredData.membershipStartDate?.format(DATE_FORMAT),
  membershipEndDate: sampleWithRequiredData.membershipEndDate?.format(DATE_FORMAT),
  dateOfJoining: sampleWithRequiredData.dateOfJoining?.format(DATE_FORMAT),
  dateOfConfirmation: sampleWithRequiredData.dateOfConfirmation?.format(DATE_FORMAT),
};

describe('PfAccount Service', () => {
  let service: PfAccountService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfAccount | IPfAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfAccountService);
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

    it('should create a PfAccount', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfAccount', () => {
      const pfAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfAccount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfAccount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfAccount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfAccountToCollectionIfMissing', () => {
      it('should add a PfAccount to an empty array', () => {
        const pfAccount: IPfAccount = sampleWithRequiredData;
        expectedResult = service.addPfAccountToCollectionIfMissing([], pfAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfAccount);
      });

      it('should not add a PfAccount to an array that contains it', () => {
        const pfAccount: IPfAccount = sampleWithRequiredData;
        const pfAccountCollection: IPfAccount[] = [
          {
            ...pfAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfAccountToCollectionIfMissing(pfAccountCollection, pfAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfAccount to an array that doesn't contain it", () => {
        const pfAccount: IPfAccount = sampleWithRequiredData;
        const pfAccountCollection: IPfAccount[] = [sampleWithPartialData];
        expectedResult = service.addPfAccountToCollectionIfMissing(pfAccountCollection, pfAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfAccount);
      });

      it('should add only unique PfAccount to an array', () => {
        const pfAccountArray: IPfAccount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfAccountCollection: IPfAccount[] = [sampleWithRequiredData];
        expectedResult = service.addPfAccountToCollectionIfMissing(pfAccountCollection, ...pfAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfAccount: IPfAccount = sampleWithRequiredData;
        const pfAccount2: IPfAccount = sampleWithPartialData;
        expectedResult = service.addPfAccountToCollectionIfMissing([], pfAccount, pfAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfAccount);
        expect(expectedResult).toContain(pfAccount2);
      });

      it('should accept null and undefined values', () => {
        const pfAccount: IPfAccount = sampleWithRequiredData;
        expectedResult = service.addPfAccountToCollectionIfMissing([], null, pfAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfAccount);
      });

      it('should return initial array if no PfAccount is added', () => {
        const pfAccountCollection: IPfAccount[] = [sampleWithRequiredData];
        expectedResult = service.addPfAccountToCollectionIfMissing(pfAccountCollection, undefined, null);
        expect(expectedResult).toEqual(pfAccountCollection);
      });
    });

    describe('comparePfAccount', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfAccount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfAccount(entity1, entity2);
        const compareResult2 = service.comparePfAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfAccount(entity1, entity2);
        const compareResult2 = service.comparePfAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfAccount(entity1, entity2);
        const compareResult2 = service.comparePfAccount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
