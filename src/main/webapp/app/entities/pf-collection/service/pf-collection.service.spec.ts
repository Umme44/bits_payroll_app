import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPfCollection } from '../pf-collection.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-collection.test-samples';

import { PfCollectionService, RestPfCollection } from './pf-collection.service';

const requireRestSample: RestPfCollection = {
  ...sampleWithRequiredData,
  transactionDate: sampleWithRequiredData.transactionDate?.format(DATE_FORMAT),
};

describe('PfCollection Service', () => {
  let service: PfCollectionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfCollection | IPfCollection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfCollectionService);
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

    it('should create a PfCollection', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfCollection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfCollection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfCollection', () => {
      const pfCollection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfCollection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfCollection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfCollection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfCollection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfCollectionToCollectionIfMissing', () => {
      it('should add a PfCollection to an empty array', () => {
        const pfCollection: IPfCollection = sampleWithRequiredData;
        expectedResult = service.addPfCollectionToCollectionIfMissing([], pfCollection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfCollection);
      });

      it('should not add a PfCollection to an array that contains it', () => {
        const pfCollection: IPfCollection = sampleWithRequiredData;
        const pfCollectionCollection: IPfCollection[] = [
          {
            ...pfCollection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfCollectionToCollectionIfMissing(pfCollectionCollection, pfCollection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfCollection to an array that doesn't contain it", () => {
        const pfCollection: IPfCollection = sampleWithRequiredData;
        const pfCollectionCollection: IPfCollection[] = [sampleWithPartialData];
        expectedResult = service.addPfCollectionToCollectionIfMissing(pfCollectionCollection, pfCollection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfCollection);
      });

      it('should add only unique PfCollection to an array', () => {
        const pfCollectionArray: IPfCollection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfCollectionCollection: IPfCollection[] = [sampleWithRequiredData];
        expectedResult = service.addPfCollectionToCollectionIfMissing(pfCollectionCollection, ...pfCollectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfCollection: IPfCollection = sampleWithRequiredData;
        const pfCollection2: IPfCollection = sampleWithPartialData;
        expectedResult = service.addPfCollectionToCollectionIfMissing([], pfCollection, pfCollection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfCollection);
        expect(expectedResult).toContain(pfCollection2);
      });

      it('should accept null and undefined values', () => {
        const pfCollection: IPfCollection = sampleWithRequiredData;
        expectedResult = service.addPfCollectionToCollectionIfMissing([], null, pfCollection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfCollection);
      });

      it('should return initial array if no PfCollection is added', () => {
        const pfCollectionCollection: IPfCollection[] = [sampleWithRequiredData];
        expectedResult = service.addPfCollectionToCollectionIfMissing(pfCollectionCollection, undefined, null);
        expect(expectedResult).toEqual(pfCollectionCollection);
      });
    });

    describe('comparePfCollection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfCollection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfCollection(entity1, entity2);
        const compareResult2 = service.comparePfCollection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfCollection(entity1, entity2);
        const compareResult2 = service.comparePfCollection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfCollection(entity1, entity2);
        const compareResult2 = service.comparePfCollection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
