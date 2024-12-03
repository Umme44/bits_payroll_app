import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPfLoan } from '../pf-loan.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-loan.test-samples';

import { PfLoanService, RestPfLoan } from './pf-loan.service';

const requireRestSample: RestPfLoan = {
  ...sampleWithRequiredData,
  disbursementDate: sampleWithRequiredData.disbursementDate?.format(DATE_FORMAT),
  instalmentStartFrom: sampleWithRequiredData.instalmentStartFrom?.format(DATE_FORMAT),
};

describe('PfLoan Service', () => {
  let service: PfLoanService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfLoan | IPfLoan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfLoanService);
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

    it('should create a PfLoan', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfLoan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfLoan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfLoan', () => {
      const pfLoan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfLoan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfLoan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfLoan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfLoan', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfLoanToCollectionIfMissing', () => {
      it('should add a PfLoan to an empty array', () => {
        const pfLoan: IPfLoan = sampleWithRequiredData;
        expectedResult = service.addPfLoanToCollectionIfMissing([], pfLoan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfLoan);
      });

      it('should not add a PfLoan to an array that contains it', () => {
        const pfLoan: IPfLoan = sampleWithRequiredData;
        const pfLoanCollection: IPfLoan[] = [
          {
            ...pfLoan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfLoanToCollectionIfMissing(pfLoanCollection, pfLoan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfLoan to an array that doesn't contain it", () => {
        const pfLoan: IPfLoan = sampleWithRequiredData;
        const pfLoanCollection: IPfLoan[] = [sampleWithPartialData];
        expectedResult = service.addPfLoanToCollectionIfMissing(pfLoanCollection, pfLoan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfLoan);
      });

      it('should add only unique PfLoan to an array', () => {
        const pfLoanArray: IPfLoan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfLoanCollection: IPfLoan[] = [sampleWithRequiredData];
        expectedResult = service.addPfLoanToCollectionIfMissing(pfLoanCollection, ...pfLoanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfLoan: IPfLoan = sampleWithRequiredData;
        const pfLoan2: IPfLoan = sampleWithPartialData;
        expectedResult = service.addPfLoanToCollectionIfMissing([], pfLoan, pfLoan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfLoan);
        expect(expectedResult).toContain(pfLoan2);
      });

      it('should accept null and undefined values', () => {
        const pfLoan: IPfLoan = sampleWithRequiredData;
        expectedResult = service.addPfLoanToCollectionIfMissing([], null, pfLoan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfLoan);
      });

      it('should return initial array if no PfLoan is added', () => {
        const pfLoanCollection: IPfLoan[] = [sampleWithRequiredData];
        expectedResult = service.addPfLoanToCollectionIfMissing(pfLoanCollection, undefined, null);
        expect(expectedResult).toEqual(pfLoanCollection);
      });
    });

    describe('comparePfLoan', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfLoan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfLoan(entity1, entity2);
        const compareResult2 = service.comparePfLoan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfLoan(entity1, entity2);
        const compareResult2 = service.comparePfLoan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfLoan(entity1, entity2);
        const compareResult2 = service.comparePfLoan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
