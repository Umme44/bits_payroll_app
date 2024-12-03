import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-loan-repayment.test-samples';

import { PfLoanRepaymentService, RestPfLoanRepayment } from './pf-loan-repayment.service';

const requireRestSample: RestPfLoanRepayment = {
  ...sampleWithRequiredData,
  deductionDate: sampleWithRequiredData.deductionDate?.format(DATE_FORMAT),
};

describe('PfLoanRepayment Service', () => {
  let service: PfLoanRepaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfLoanRepayment | IPfLoanRepayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfLoanRepaymentService);
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

    it('should create a PfLoanRepayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfLoanRepayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfLoanRepayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfLoanRepayment', () => {
      const pfLoanRepayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfLoanRepayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfLoanRepayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfLoanRepayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfLoanRepayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfLoanRepaymentToCollectionIfMissing', () => {
      it('should add a PfLoanRepayment to an empty array', () => {
        const pfLoanRepayment: IPfLoanRepayment = sampleWithRequiredData;
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing([], pfLoanRepayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfLoanRepayment);
      });

      it('should not add a PfLoanRepayment to an array that contains it', () => {
        const pfLoanRepayment: IPfLoanRepayment = sampleWithRequiredData;
        const pfLoanRepaymentCollection: IPfLoanRepayment[] = [
          {
            ...pfLoanRepayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing(pfLoanRepaymentCollection, pfLoanRepayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfLoanRepayment to an array that doesn't contain it", () => {
        const pfLoanRepayment: IPfLoanRepayment = sampleWithRequiredData;
        const pfLoanRepaymentCollection: IPfLoanRepayment[] = [sampleWithPartialData];
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing(pfLoanRepaymentCollection, pfLoanRepayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfLoanRepayment);
      });

      it('should add only unique PfLoanRepayment to an array', () => {
        const pfLoanRepaymentArray: IPfLoanRepayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfLoanRepaymentCollection: IPfLoanRepayment[] = [sampleWithRequiredData];
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing(pfLoanRepaymentCollection, ...pfLoanRepaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfLoanRepayment: IPfLoanRepayment = sampleWithRequiredData;
        const pfLoanRepayment2: IPfLoanRepayment = sampleWithPartialData;
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing([], pfLoanRepayment, pfLoanRepayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfLoanRepayment);
        expect(expectedResult).toContain(pfLoanRepayment2);
      });

      it('should accept null and undefined values', () => {
        const pfLoanRepayment: IPfLoanRepayment = sampleWithRequiredData;
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing([], null, pfLoanRepayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfLoanRepayment);
      });

      it('should return initial array if no PfLoanRepayment is added', () => {
        const pfLoanRepaymentCollection: IPfLoanRepayment[] = [sampleWithRequiredData];
        expectedResult = service.addPfLoanRepaymentToCollectionIfMissing(pfLoanRepaymentCollection, undefined, null);
        expect(expectedResult).toEqual(pfLoanRepaymentCollection);
      });
    });

    describe('comparePfLoanRepayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfLoanRepayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfLoanRepayment(entity1, entity2);
        const compareResult2 = service.comparePfLoanRepayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfLoanRepayment(entity1, entity2);
        const compareResult2 = service.comparePfLoanRepayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfLoanRepayment(entity1, entity2);
        const compareResult2 = service.comparePfLoanRepayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
