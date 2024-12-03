import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISpecialShiftTiming } from '../special-shift-timing.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../special-shift-timing.test-samples';

import { SpecialShiftTimingService, RestSpecialShiftTiming } from './special-shift-timing.service';

const requireRestSample: RestSpecialShiftTiming = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('SpecialShiftTiming Service', () => {
  let service: SpecialShiftTimingService;
  let httpMock: HttpTestingController;
  let expectedResult: ISpecialShiftTiming | ISpecialShiftTiming[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpecialShiftTimingService);
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

    it('should create a SpecialShiftTiming', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const specialShiftTiming = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(specialShiftTiming).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SpecialShiftTiming', () => {
      const specialShiftTiming = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(specialShiftTiming).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SpecialShiftTiming', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SpecialShiftTiming', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SpecialShiftTiming', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSpecialShiftTimingToCollectionIfMissing', () => {
      it('should add a SpecialShiftTiming to an empty array', () => {
        const specialShiftTiming: ISpecialShiftTiming = sampleWithRequiredData;
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing([], specialShiftTiming);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(specialShiftTiming);
      });

      it('should not add a SpecialShiftTiming to an array that contains it', () => {
        const specialShiftTiming: ISpecialShiftTiming = sampleWithRequiredData;
        const specialShiftTimingCollection: ISpecialShiftTiming[] = [
          {
            ...specialShiftTiming,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing(specialShiftTimingCollection, specialShiftTiming);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SpecialShiftTiming to an array that doesn't contain it", () => {
        const specialShiftTiming: ISpecialShiftTiming = sampleWithRequiredData;
        const specialShiftTimingCollection: ISpecialShiftTiming[] = [sampleWithPartialData];
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing(specialShiftTimingCollection, specialShiftTiming);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(specialShiftTiming);
      });

      it('should add only unique SpecialShiftTiming to an array', () => {
        const specialShiftTimingArray: ISpecialShiftTiming[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const specialShiftTimingCollection: ISpecialShiftTiming[] = [sampleWithRequiredData];
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing(specialShiftTimingCollection, ...specialShiftTimingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const specialShiftTiming: ISpecialShiftTiming = sampleWithRequiredData;
        const specialShiftTiming2: ISpecialShiftTiming = sampleWithPartialData;
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing([], specialShiftTiming, specialShiftTiming2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(specialShiftTiming);
        expect(expectedResult).toContain(specialShiftTiming2);
      });

      it('should accept null and undefined values', () => {
        const specialShiftTiming: ISpecialShiftTiming = sampleWithRequiredData;
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing([], null, specialShiftTiming, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(specialShiftTiming);
      });

      it('should return initial array if no SpecialShiftTiming is added', () => {
        const specialShiftTimingCollection: ISpecialShiftTiming[] = [sampleWithRequiredData];
        expectedResult = service.addSpecialShiftTimingToCollectionIfMissing(specialShiftTimingCollection, undefined, null);
        expect(expectedResult).toEqual(specialShiftTimingCollection);
      });
    });

    describe('compareSpecialShiftTiming', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSpecialShiftTiming(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSpecialShiftTiming(entity1, entity2);
        const compareResult2 = service.compareSpecialShiftTiming(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSpecialShiftTiming(entity1, entity2);
        const compareResult2 = service.compareSpecialShiftTiming(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSpecialShiftTiming(entity1, entity2);
        const compareResult2 = service.compareSpecialShiftTiming(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
