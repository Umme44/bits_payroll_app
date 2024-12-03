import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../festival-bonus-details.test-samples';

import { FestivalBonusDetailsService } from './festival-bonus-details.service';

const requireRestSample: IFestivalBonusDetails = {
  ...sampleWithRequiredData,
};

describe('FestivalBonusDetails Service', () => {
  let service: FestivalBonusDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IFestivalBonusDetails | IFestivalBonusDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FestivalBonusDetailsService);
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

    it('should create a FestivalBonusDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const festivalBonusDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(festivalBonusDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FestivalBonusDetails', () => {
      const festivalBonusDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(festivalBonusDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FestivalBonusDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FestivalBonusDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FestivalBonusDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFestivalBonusDetailsToCollectionIfMissing', () => {
      it('should add a FestivalBonusDetails to an empty array', () => {
        const festivalBonusDetails: IFestivalBonusDetails = sampleWithRequiredData;
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing([], festivalBonusDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festivalBonusDetails);
      });

      it('should not add a FestivalBonusDetails to an array that contains it', () => {
        const festivalBonusDetails: IFestivalBonusDetails = sampleWithRequiredData;
        const festivalBonusDetailsCollection: IFestivalBonusDetails[] = [
          {
            ...festivalBonusDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing(festivalBonusDetailsCollection, festivalBonusDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FestivalBonusDetails to an array that doesn't contain it", () => {
        const festivalBonusDetails: IFestivalBonusDetails = sampleWithRequiredData;
        const festivalBonusDetailsCollection: IFestivalBonusDetails[] = [sampleWithPartialData];
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing(festivalBonusDetailsCollection, festivalBonusDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festivalBonusDetails);
      });

      it('should add only unique FestivalBonusDetails to an array', () => {
        const festivalBonusDetailsArray: IFestivalBonusDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const festivalBonusDetailsCollection: IFestivalBonusDetails[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing(festivalBonusDetailsCollection, ...festivalBonusDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const festivalBonusDetails: IFestivalBonusDetails = sampleWithRequiredData;
        const festivalBonusDetails2: IFestivalBonusDetails = sampleWithPartialData;
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing([], festivalBonusDetails, festivalBonusDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festivalBonusDetails);
        expect(expectedResult).toContain(festivalBonusDetails2);
      });

      it('should accept null and undefined values', () => {
        const festivalBonusDetails: IFestivalBonusDetails = sampleWithRequiredData;
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing([], null, festivalBonusDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festivalBonusDetails);
      });

      it('should return initial array if no FestivalBonusDetails is added', () => {
        const festivalBonusDetailsCollection: IFestivalBonusDetails[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalBonusDetailsToCollectionIfMissing(festivalBonusDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(festivalBonusDetailsCollection);
      });
    });

    describe('compareFestivalBonusDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFestivalBonusDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFestivalBonusDetails(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFestivalBonusDetails(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFestivalBonusDetails(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
