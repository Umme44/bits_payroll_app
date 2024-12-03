import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../income-tax-challan.test-samples';

import { IncomeTaxChallanService, RestIncomeTaxChallan } from './income-tax-challan.service';

const requireRestSample: RestIncomeTaxChallan = {
  ...sampleWithRequiredData,
  challanDate: sampleWithRequiredData.challanDate?.format(DATE_FORMAT),
};

describe('IncomeTaxChallan Service', () => {
  let service: IncomeTaxChallanService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncomeTaxChallan | IIncomeTaxChallan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IncomeTaxChallanService);
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

    it('should create a IncomeTaxChallan', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const incomeTaxChallan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incomeTaxChallan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncomeTaxChallan', () => {
      const incomeTaxChallan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incomeTaxChallan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncomeTaxChallan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncomeTaxChallan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncomeTaxChallan', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIncomeTaxChallanToCollectionIfMissing', () => {
      it('should add a IncomeTaxChallan to an empty array', () => {
        const incomeTaxChallan: IIncomeTaxChallan = sampleWithRequiredData;
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing([], incomeTaxChallan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeTaxChallan);
      });

      it('should not add a IncomeTaxChallan to an array that contains it', () => {
        const incomeTaxChallan: IIncomeTaxChallan = sampleWithRequiredData;
        const incomeTaxChallanCollection: IIncomeTaxChallan[] = [
          {
            ...incomeTaxChallan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing(incomeTaxChallanCollection, incomeTaxChallan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncomeTaxChallan to an array that doesn't contain it", () => {
        const incomeTaxChallan: IIncomeTaxChallan = sampleWithRequiredData;
        const incomeTaxChallanCollection: IIncomeTaxChallan[] = [sampleWithPartialData];
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing(incomeTaxChallanCollection, incomeTaxChallan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeTaxChallan);
      });

      it('should add only unique IncomeTaxChallan to an array', () => {
        const incomeTaxChallanArray: IIncomeTaxChallan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incomeTaxChallanCollection: IIncomeTaxChallan[] = [sampleWithRequiredData];
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing(incomeTaxChallanCollection, ...incomeTaxChallanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incomeTaxChallan: IIncomeTaxChallan = sampleWithRequiredData;
        const incomeTaxChallan2: IIncomeTaxChallan = sampleWithPartialData;
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing([], incomeTaxChallan, incomeTaxChallan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeTaxChallan);
        expect(expectedResult).toContain(incomeTaxChallan2);
      });

      it('should accept null and undefined values', () => {
        const incomeTaxChallan: IIncomeTaxChallan = sampleWithRequiredData;
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing([], null, incomeTaxChallan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeTaxChallan);
      });

      it('should return initial array if no IncomeTaxChallan is added', () => {
        const incomeTaxChallanCollection: IIncomeTaxChallan[] = [sampleWithRequiredData];
        expectedResult = service.addIncomeTaxChallanToCollectionIfMissing(incomeTaxChallanCollection, undefined, null);
        expect(expectedResult).toEqual(incomeTaxChallanCollection);
      });
    });

    describe('compareIncomeTaxChallan', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncomeTaxChallan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIncomeTaxChallan(entity1, entity2);
        const compareResult2 = service.compareIncomeTaxChallan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIncomeTaxChallan(entity1, entity2);
        const compareResult2 = service.compareIncomeTaxChallan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIncomeTaxChallan(entity1, entity2);
        const compareResult2 = service.compareIncomeTaxChallan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
