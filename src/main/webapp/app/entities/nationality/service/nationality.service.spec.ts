import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INationality } from '../nationality.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../nationality.test-samples';

import { NationalityService } from './nationality.service';

const requireRestSample: INationality = {
  ...sampleWithRequiredData,
};

describe('Nationality Service', () => {
  let service: NationalityService;
  let httpMock: HttpTestingController;
  let expectedResult: INationality | INationality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NationalityService);
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

    it('should create a Nationality', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const nationality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(nationality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Nationality', () => {
      const nationality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(nationality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Nationality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Nationality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Nationality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNationalityToCollectionIfMissing', () => {
      it('should add a Nationality to an empty array', () => {
        const nationality: INationality = sampleWithRequiredData;
        expectedResult = service.addNationalityToCollectionIfMissing([], nationality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nationality);
      });

      it('should not add a Nationality to an array that contains it', () => {
        const nationality: INationality = sampleWithRequiredData;
        const nationalityCollection: INationality[] = [
          {
            ...nationality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNationalityToCollectionIfMissing(nationalityCollection, nationality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Nationality to an array that doesn't contain it", () => {
        const nationality: INationality = sampleWithRequiredData;
        const nationalityCollection: INationality[] = [sampleWithPartialData];
        expectedResult = service.addNationalityToCollectionIfMissing(nationalityCollection, nationality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nationality);
      });

      it('should add only unique Nationality to an array', () => {
        const nationalityArray: INationality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const nationalityCollection: INationality[] = [sampleWithRequiredData];
        expectedResult = service.addNationalityToCollectionIfMissing(nationalityCollection, ...nationalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nationality: INationality = sampleWithRequiredData;
        const nationality2: INationality = sampleWithPartialData;
        expectedResult = service.addNationalityToCollectionIfMissing([], nationality, nationality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nationality);
        expect(expectedResult).toContain(nationality2);
      });

      it('should accept null and undefined values', () => {
        const nationality: INationality = sampleWithRequiredData;
        expectedResult = service.addNationalityToCollectionIfMissing([], null, nationality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nationality);
      });

      it('should return initial array if no Nationality is added', () => {
        const nationalityCollection: INationality[] = [sampleWithRequiredData];
        expectedResult = service.addNationalityToCollectionIfMissing(nationalityCollection, undefined, null);
        expect(expectedResult).toEqual(nationalityCollection);
      });
    });

    describe('compareNationality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNationality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNationality(entity1, entity2);
        const compareResult2 = service.compareNationality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNationality(entity1, entity2);
        const compareResult2 = service.compareNationality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNationality(entity1, entity2);
        const compareResult2 = service.compareNationality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
