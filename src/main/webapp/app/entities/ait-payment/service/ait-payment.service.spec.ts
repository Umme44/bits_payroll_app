import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAitPayment } from '../ait-payment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ait-payment.test-samples';

import { AitPaymentService, RestAitPayment } from './ait-payment.service';

const requireRestSample: RestAitPayment = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('AitPayment Service', () => {
  let service: AitPaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IAitPayment | IAitPayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AitPaymentService);
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

    it('should create a AitPayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const aitPayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aitPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AitPayment', () => {
      const aitPayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aitPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AitPayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AitPayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AitPayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAitPaymentToCollectionIfMissing', () => {
      it('should add a AitPayment to an empty array', () => {
        const aitPayment: IAitPayment = sampleWithRequiredData;
        expectedResult = service.addAitPaymentToCollectionIfMissing([], aitPayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aitPayment);
      });

      it('should not add a AitPayment to an array that contains it', () => {
        const aitPayment: IAitPayment = sampleWithRequiredData;
        const aitPaymentCollection: IAitPayment[] = [
          {
            ...aitPayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAitPaymentToCollectionIfMissing(aitPaymentCollection, aitPayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AitPayment to an array that doesn't contain it", () => {
        const aitPayment: IAitPayment = sampleWithRequiredData;
        const aitPaymentCollection: IAitPayment[] = [sampleWithPartialData];
        expectedResult = service.addAitPaymentToCollectionIfMissing(aitPaymentCollection, aitPayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aitPayment);
      });

      it('should add only unique AitPayment to an array', () => {
        const aitPaymentArray: IAitPayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aitPaymentCollection: IAitPayment[] = [sampleWithRequiredData];
        expectedResult = service.addAitPaymentToCollectionIfMissing(aitPaymentCollection, ...aitPaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aitPayment: IAitPayment = sampleWithRequiredData;
        const aitPayment2: IAitPayment = sampleWithPartialData;
        expectedResult = service.addAitPaymentToCollectionIfMissing([], aitPayment, aitPayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aitPayment);
        expect(expectedResult).toContain(aitPayment2);
      });

      it('should accept null and undefined values', () => {
        const aitPayment: IAitPayment = sampleWithRequiredData;
        expectedResult = service.addAitPaymentToCollectionIfMissing([], null, aitPayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aitPayment);
      });

      it('should return initial array if no AitPayment is added', () => {
        const aitPaymentCollection: IAitPayment[] = [sampleWithRequiredData];
        expectedResult = service.addAitPaymentToCollectionIfMissing(aitPaymentCollection, undefined, null);
        expect(expectedResult).toEqual(aitPaymentCollection);
      });
    });

    describe('compareAitPayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAitPayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAitPayment(entity1, entity2);
        const compareResult2 = service.compareAitPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAitPayment(entity1, entity2);
        const compareResult2 = service.compareAitPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAitPayment(entity1, entity2);
        const compareResult2 = service.compareAitPayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
