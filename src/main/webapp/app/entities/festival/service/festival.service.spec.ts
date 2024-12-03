import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFestival } from '../festival.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../festival.test-samples';

import { FestivalService, RestFestival } from './festival.service';

const requireRestSample: RestFestival = {
  ...sampleWithRequiredData,
  festivalDate: sampleWithRequiredData.festivalDate?.format(DATE_FORMAT),
  bonusDisbursementDate: sampleWithRequiredData.bonusDisbursementDate?.format(DATE_FORMAT),
};

describe('Festival Service', () => {
  let service: FestivalService;
  let httpMock: HttpTestingController;
  let expectedResult: IFestival | IFestival[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FestivalService);
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

    it('should create a Festival', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const festival = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(festival).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Festival', () => {
      const festival = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(festival).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Festival', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Festival', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Festival', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFestivalToCollectionIfMissing', () => {
      it('should add a Festival to an empty array', () => {
        const festival: IFestival = sampleWithRequiredData;
        expectedResult = service.addFestivalToCollectionIfMissing([], festival);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festival);
      });

      it('should not add a Festival to an array that contains it', () => {
        const festival: IFestival = sampleWithRequiredData;
        const festivalCollection: IFestival[] = [
          {
            ...festival,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFestivalToCollectionIfMissing(festivalCollection, festival);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Festival to an array that doesn't contain it", () => {
        const festival: IFestival = sampleWithRequiredData;
        const festivalCollection: IFestival[] = [sampleWithPartialData];
        expectedResult = service.addFestivalToCollectionIfMissing(festivalCollection, festival);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festival);
      });

      it('should add only unique Festival to an array', () => {
        const festivalArray: IFestival[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const festivalCollection: IFestival[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalToCollectionIfMissing(festivalCollection, ...festivalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const festival: IFestival = sampleWithRequiredData;
        const festival2: IFestival = sampleWithPartialData;
        expectedResult = service.addFestivalToCollectionIfMissing([], festival, festival2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festival);
        expect(expectedResult).toContain(festival2);
      });

      it('should accept null and undefined values', () => {
        const festival: IFestival = sampleWithRequiredData;
        expectedResult = service.addFestivalToCollectionIfMissing([], null, festival, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festival);
      });

      it('should return initial array if no Festival is added', () => {
        const festivalCollection: IFestival[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalToCollectionIfMissing(festivalCollection, undefined, null);
        expect(expectedResult).toEqual(festivalCollection);
      });
    });

    describe('compareFestival', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFestival(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFestival(entity1, entity2);
        const compareResult2 = service.compareFestival(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFestival(entity1, entity2);
        const compareResult2 = service.compareFestival(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFestival(entity1, entity2);
        const compareResult2 = service.compareFestival(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
