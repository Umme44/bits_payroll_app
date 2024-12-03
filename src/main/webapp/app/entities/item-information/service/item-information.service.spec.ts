import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemInformation } from '../item-information.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../item-information.test-samples';

import { ItemInformationService, RestItemInformation } from './item-information.service';

const requireRestSample: RestItemInformation = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('ItemInformation Service', () => {
  let service: ItemInformationService;
  let httpMock: HttpTestingController;
  let expectedResult: IItemInformation | IItemInformation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemInformationService);
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

    it('should create a ItemInformation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const itemInformation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(itemInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemInformation', () => {
      const itemInformation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(itemInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemInformation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemInformation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ItemInformation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addItemInformationToCollectionIfMissing', () => {
      it('should add a ItemInformation to an empty array', () => {
        const itemInformation: IItemInformation = sampleWithRequiredData;
        expectedResult = service.addItemInformationToCollectionIfMissing([], itemInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemInformation);
      });

      it('should not add a ItemInformation to an array that contains it', () => {
        const itemInformation: IItemInformation = sampleWithRequiredData;
        const itemInformationCollection: IItemInformation[] = [
          {
            ...itemInformation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addItemInformationToCollectionIfMissing(itemInformationCollection, itemInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemInformation to an array that doesn't contain it", () => {
        const itemInformation: IItemInformation = sampleWithRequiredData;
        const itemInformationCollection: IItemInformation[] = [sampleWithPartialData];
        expectedResult = service.addItemInformationToCollectionIfMissing(itemInformationCollection, itemInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemInformation);
      });

      it('should add only unique ItemInformation to an array', () => {
        const itemInformationArray: IItemInformation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const itemInformationCollection: IItemInformation[] = [sampleWithRequiredData];
        expectedResult = service.addItemInformationToCollectionIfMissing(itemInformationCollection, ...itemInformationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemInformation: IItemInformation = sampleWithRequiredData;
        const itemInformation2: IItemInformation = sampleWithPartialData;
        expectedResult = service.addItemInformationToCollectionIfMissing([], itemInformation, itemInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemInformation);
        expect(expectedResult).toContain(itemInformation2);
      });

      it('should accept null and undefined values', () => {
        const itemInformation: IItemInformation = sampleWithRequiredData;
        expectedResult = service.addItemInformationToCollectionIfMissing([], null, itemInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemInformation);
      });

      it('should return initial array if no ItemInformation is added', () => {
        const itemInformationCollection: IItemInformation[] = [sampleWithRequiredData];
        expectedResult = service.addItemInformationToCollectionIfMissing(itemInformationCollection, undefined, null);
        expect(expectedResult).toEqual(itemInformationCollection);
      });
    });

    describe('compareItemInformation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareItemInformation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareItemInformation(entity1, entity2);
        const compareResult2 = service.compareItemInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareItemInformation(entity1, entity2);
        const compareResult2 = service.compareItemInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareItemInformation(entity1, entity2);
        const compareResult2 = service.compareItemInformation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
