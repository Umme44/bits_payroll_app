import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../pro-rata-festival-bonus.test-samples';

import { ProRataFestivalBonusService, RestProRataFestivalBonus } from './pro-rata-festival-bonus.service';

const requireRestSample: RestProRataFestivalBonus = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('ProRataFestivalBonus Service', () => {
  let service: ProRataFestivalBonusService;
  let httpMock: HttpTestingController;
  let expectedResult: IProRataFestivalBonus | IProRataFestivalBonus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProRataFestivalBonusService);
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

    it('should create a ProRataFestivalBonus', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const proRataFestivalBonus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(proRataFestivalBonus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProRataFestivalBonus', () => {
      const proRataFestivalBonus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(proRataFestivalBonus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProRataFestivalBonus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProRataFestivalBonus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProRataFestivalBonus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProRataFestivalBonusToCollectionIfMissing', () => {
      it('should add a ProRataFestivalBonus to an empty array', () => {
        const proRataFestivalBonus: IProRataFestivalBonus = sampleWithRequiredData;
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing([], proRataFestivalBonus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proRataFestivalBonus);
      });

      it('should not add a ProRataFestivalBonus to an array that contains it', () => {
        const proRataFestivalBonus: IProRataFestivalBonus = sampleWithRequiredData;
        const proRataFestivalBonusCollection: IProRataFestivalBonus[] = [
          {
            ...proRataFestivalBonus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing(proRataFestivalBonusCollection, proRataFestivalBonus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProRataFestivalBonus to an array that doesn't contain it", () => {
        const proRataFestivalBonus: IProRataFestivalBonus = sampleWithRequiredData;
        const proRataFestivalBonusCollection: IProRataFestivalBonus[] = [sampleWithPartialData];
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing(proRataFestivalBonusCollection, proRataFestivalBonus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proRataFestivalBonus);
      });

      it('should add only unique ProRataFestivalBonus to an array', () => {
        const proRataFestivalBonusArray: IProRataFestivalBonus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const proRataFestivalBonusCollection: IProRataFestivalBonus[] = [sampleWithRequiredData];
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing(proRataFestivalBonusCollection, ...proRataFestivalBonusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proRataFestivalBonus: IProRataFestivalBonus = sampleWithRequiredData;
        const proRataFestivalBonus2: IProRataFestivalBonus = sampleWithPartialData;
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing([], proRataFestivalBonus, proRataFestivalBonus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proRataFestivalBonus);
        expect(expectedResult).toContain(proRataFestivalBonus2);
      });

      it('should accept null and undefined values', () => {
        const proRataFestivalBonus: IProRataFestivalBonus = sampleWithRequiredData;
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing([], null, proRataFestivalBonus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proRataFestivalBonus);
      });

      it('should return initial array if no ProRataFestivalBonus is added', () => {
        const proRataFestivalBonusCollection: IProRataFestivalBonus[] = [sampleWithRequiredData];
        expectedResult = service.addProRataFestivalBonusToCollectionIfMissing(proRataFestivalBonusCollection, undefined, null);
        expect(expectedResult).toEqual(proRataFestivalBonusCollection);
      });
    });

    describe('compareProRataFestivalBonus', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProRataFestivalBonus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProRataFestivalBonus(entity1, entity2);
        const compareResult2 = service.compareProRataFestivalBonus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProRataFestivalBonus(entity1, entity2);
        const compareResult2 = service.compareProRataFestivalBonus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProRataFestivalBonus(entity1, entity2);
        const compareResult2 = service.compareProRataFestivalBonus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
