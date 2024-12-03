import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPfArrear } from '../pf-arrear.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-arrear.test-samples';

import { PfArrearService } from './pf-arrear.service';

const requireRestSample: IPfArrear = {
  ...sampleWithRequiredData,
};

describe('PfArrear Service', () => {
  let service: PfArrearService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfArrear | IPfArrear[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfArrearService);
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

    it('should create a PfArrear', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfArrear = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfArrear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfArrear', () => {
      const pfArrear = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfArrear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfArrear', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfArrear', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfArrear', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfArrearToCollectionIfMissing', () => {
      it('should add a PfArrear to an empty array', () => {
        const pfArrear: IPfArrear = sampleWithRequiredData;
        expectedResult = service.addPfArrearToCollectionIfMissing([], pfArrear);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfArrear);
      });

      it('should not add a PfArrear to an array that contains it', () => {
        const pfArrear: IPfArrear = sampleWithRequiredData;
        const pfArrearCollection: IPfArrear[] = [
          {
            ...pfArrear,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfArrearToCollectionIfMissing(pfArrearCollection, pfArrear);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfArrear to an array that doesn't contain it", () => {
        const pfArrear: IPfArrear = sampleWithRequiredData;
        const pfArrearCollection: IPfArrear[] = [sampleWithPartialData];
        expectedResult = service.addPfArrearToCollectionIfMissing(pfArrearCollection, pfArrear);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfArrear);
      });

      it('should add only unique PfArrear to an array', () => {
        const pfArrearArray: IPfArrear[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfArrearCollection: IPfArrear[] = [sampleWithRequiredData];
        expectedResult = service.addPfArrearToCollectionIfMissing(pfArrearCollection, ...pfArrearArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfArrear: IPfArrear = sampleWithRequiredData;
        const pfArrear2: IPfArrear = sampleWithPartialData;
        expectedResult = service.addPfArrearToCollectionIfMissing([], pfArrear, pfArrear2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfArrear);
        expect(expectedResult).toContain(pfArrear2);
      });

      it('should accept null and undefined values', () => {
        const pfArrear: IPfArrear = sampleWithRequiredData;
        expectedResult = service.addPfArrearToCollectionIfMissing([], null, pfArrear, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfArrear);
      });

      it('should return initial array if no PfArrear is added', () => {
        const pfArrearCollection: IPfArrear[] = [sampleWithRequiredData];
        expectedResult = service.addPfArrearToCollectionIfMissing(pfArrearCollection, undefined, null);
        expect(expectedResult).toEqual(pfArrearCollection);
      });
    });

    describe('comparePfArrear', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfArrear(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfArrear(entity1, entity2);
        const compareResult2 = service.comparePfArrear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfArrear(entity1, entity2);
        const compareResult2 = service.comparePfArrear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfArrear(entity1, entity2);
        const compareResult2 = service.comparePfArrear(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
