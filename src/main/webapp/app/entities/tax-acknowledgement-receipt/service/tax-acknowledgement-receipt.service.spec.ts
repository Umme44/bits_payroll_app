import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITaxAcknowledgementReceipt } from '../tax-acknowledgement-receipt.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../tax-acknowledgement-receipt.test-samples';

import { TaxAcknowledgementReceiptService, RestTaxAcknowledgementReceipt } from './tax-acknowledgement-receipt.service';

const requireRestSample: RestTaxAcknowledgementReceipt = {
  ...sampleWithRequiredData,
  dateOfSubmission: sampleWithRequiredData.dateOfSubmission?.format(DATE_FORMAT),
  receivedAt: sampleWithRequiredData.receivedAt?.toJSON(),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('TaxAcknowledgementReceipt Service', () => {
  let service: TaxAcknowledgementReceiptService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxAcknowledgementReceipt | ITaxAcknowledgementReceipt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TaxAcknowledgementReceiptService);
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

    it('should create a TaxAcknowledgementReceipt', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const taxAcknowledgementReceipt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxAcknowledgementReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxAcknowledgementReceipt', () => {
      const taxAcknowledgementReceipt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxAcknowledgementReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaxAcknowledgementReceipt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxAcknowledgementReceipt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TaxAcknowledgementReceipt', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTaxAcknowledgementReceiptToCollectionIfMissing', () => {
      it('should add a TaxAcknowledgementReceipt to an empty array', () => {
        const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = sampleWithRequiredData;
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing([], taxAcknowledgementReceipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxAcknowledgementReceipt);
      });

      it('should not add a TaxAcknowledgementReceipt to an array that contains it', () => {
        const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = sampleWithRequiredData;
        const taxAcknowledgementReceiptCollection: ITaxAcknowledgementReceipt[] = [
          {
            ...taxAcknowledgementReceipt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing(
          taxAcknowledgementReceiptCollection,
          taxAcknowledgementReceipt
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxAcknowledgementReceipt to an array that doesn't contain it", () => {
        const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = sampleWithRequiredData;
        const taxAcknowledgementReceiptCollection: ITaxAcknowledgementReceipt[] = [sampleWithPartialData];
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing(
          taxAcknowledgementReceiptCollection,
          taxAcknowledgementReceipt
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxAcknowledgementReceipt);
      });

      it('should add only unique TaxAcknowledgementReceipt to an array', () => {
        const taxAcknowledgementReceiptArray: ITaxAcknowledgementReceipt[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const taxAcknowledgementReceiptCollection: ITaxAcknowledgementReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing(
          taxAcknowledgementReceiptCollection,
          ...taxAcknowledgementReceiptArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = sampleWithRequiredData;
        const taxAcknowledgementReceipt2: ITaxAcknowledgementReceipt = sampleWithPartialData;
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing(
          [],
          taxAcknowledgementReceipt,
          taxAcknowledgementReceipt2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxAcknowledgementReceipt);
        expect(expectedResult).toContain(taxAcknowledgementReceipt2);
      });

      it('should accept null and undefined values', () => {
        const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = sampleWithRequiredData;
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing([], null, taxAcknowledgementReceipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxAcknowledgementReceipt);
      });

      it('should return initial array if no TaxAcknowledgementReceipt is added', () => {
        const taxAcknowledgementReceiptCollection: ITaxAcknowledgementReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAcknowledgementReceiptToCollectionIfMissing(taxAcknowledgementReceiptCollection, undefined, null);
        expect(expectedResult).toEqual(taxAcknowledgementReceiptCollection);
      });
    });

    describe('compareTaxAcknowledgementReceipt', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxAcknowledgementReceipt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTaxAcknowledgementReceipt(entity1, entity2);
        const compareResult2 = service.compareTaxAcknowledgementReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTaxAcknowledgementReceipt(entity1, entity2);
        const compareResult2 = service.compareTaxAcknowledgementReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTaxAcknowledgementReceipt(entity1, entity2);
        const compareResult2 = service.compareTaxAcknowledgementReceipt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
