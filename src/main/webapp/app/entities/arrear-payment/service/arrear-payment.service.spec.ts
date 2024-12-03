import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IArrearPayment } from '../arrear-payment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../arrear-payment.test-samples';

import { ArrearPaymentService, RestArrearPayment } from './arrear-payment.service';

const requireRestSample: RestArrearPayment = {
  ...sampleWithRequiredData,
  disbursementDate: sampleWithRequiredData.disbursementDate?.format(DATE_FORMAT),
};

describe('ArrearPayment Service', () => {
  let service: ArrearPaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IArrearPayment | IArrearPayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArrearPaymentService);
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

    it('should create a ArrearPayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const arrearPayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(arrearPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArrearPayment', () => {
      const arrearPayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(arrearPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArrearPayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArrearPayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArrearPayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArrearPaymentToCollectionIfMissing', () => {
      it('should add a ArrearPayment to an empty array', () => {
        const arrearPayment: IArrearPayment = sampleWithRequiredData;
        expectedResult = service.addArrearPaymentToCollectionIfMissing([], arrearPayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearPayment);
      });

      it('should not add a ArrearPayment to an array that contains it', () => {
        const arrearPayment: IArrearPayment = sampleWithRequiredData;
        const arrearPaymentCollection: IArrearPayment[] = [
          {
            ...arrearPayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArrearPaymentToCollectionIfMissing(arrearPaymentCollection, arrearPayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArrearPayment to an array that doesn't contain it", () => {
        const arrearPayment: IArrearPayment = sampleWithRequiredData;
        const arrearPaymentCollection: IArrearPayment[] = [sampleWithPartialData];
        expectedResult = service.addArrearPaymentToCollectionIfMissing(arrearPaymentCollection, arrearPayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearPayment);
      });

      it('should add only unique ArrearPayment to an array', () => {
        const arrearPaymentArray: IArrearPayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const arrearPaymentCollection: IArrearPayment[] = [sampleWithRequiredData];
        expectedResult = service.addArrearPaymentToCollectionIfMissing(arrearPaymentCollection, ...arrearPaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arrearPayment: IArrearPayment = sampleWithRequiredData;
        const arrearPayment2: IArrearPayment = sampleWithPartialData;
        expectedResult = service.addArrearPaymentToCollectionIfMissing([], arrearPayment, arrearPayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearPayment);
        expect(expectedResult).toContain(arrearPayment2);
      });

      it('should accept null and undefined values', () => {
        const arrearPayment: IArrearPayment = sampleWithRequiredData;
        expectedResult = service.addArrearPaymentToCollectionIfMissing([], null, arrearPayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearPayment);
      });

      it('should return initial array if no ArrearPayment is added', () => {
        const arrearPaymentCollection: IArrearPayment[] = [sampleWithRequiredData];
        expectedResult = service.addArrearPaymentToCollectionIfMissing(arrearPaymentCollection, undefined, null);
        expect(expectedResult).toEqual(arrearPaymentCollection);
      });
    });

    describe('compareArrearPayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArrearPayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareArrearPayment(entity1, entity2);
        const compareResult2 = service.compareArrearPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareArrearPayment(entity1, entity2);
        const compareResult2 = service.compareArrearPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareArrearPayment(entity1, entity2);
        const compareResult2 = service.compareArrearPayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
