import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITrainingHistory } from '../training-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../training-history.test-samples';

import { TrainingHistoryService, RestTrainingHistory } from './training-history.service';

const requireRestSample: RestTrainingHistory = {
  ...sampleWithRequiredData,
  dateOfCompletion: sampleWithRequiredData.dateOfCompletion?.format(DATE_FORMAT),
};

describe('TrainingHistory Service', () => {
  let service: TrainingHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrainingHistory | ITrainingHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrainingHistoryService);
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

    it('should create a TrainingHistory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const trainingHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trainingHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrainingHistory', () => {
      const trainingHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trainingHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrainingHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrainingHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrainingHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrainingHistoryToCollectionIfMissing', () => {
      it('should add a TrainingHistory to an empty array', () => {
        const trainingHistory: ITrainingHistory = sampleWithRequiredData;
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], trainingHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should not add a TrainingHistory to an array that contains it', () => {
        const trainingHistory: ITrainingHistory = sampleWithRequiredData;
        const trainingHistoryCollection: ITrainingHistory[] = [
          {
            ...trainingHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, trainingHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrainingHistory to an array that doesn't contain it", () => {
        const trainingHistory: ITrainingHistory = sampleWithRequiredData;
        const trainingHistoryCollection: ITrainingHistory[] = [sampleWithPartialData];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, trainingHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should add only unique TrainingHistory to an array', () => {
        const trainingHistoryArray: ITrainingHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trainingHistoryCollection: ITrainingHistory[] = [sampleWithRequiredData];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, ...trainingHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trainingHistory: ITrainingHistory = sampleWithRequiredData;
        const trainingHistory2: ITrainingHistory = sampleWithPartialData;
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], trainingHistory, trainingHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingHistory);
        expect(expectedResult).toContain(trainingHistory2);
      });

      it('should accept null and undefined values', () => {
        const trainingHistory: ITrainingHistory = sampleWithRequiredData;
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], null, trainingHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should return initial array if no TrainingHistory is added', () => {
        const trainingHistoryCollection: ITrainingHistory[] = [sampleWithRequiredData];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(trainingHistoryCollection);
      });
    });

    describe('compareTrainingHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrainingHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTrainingHistory(entity1, entity2);
        const compareResult2 = service.compareTrainingHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTrainingHistory(entity1, entity2);
        const compareResult2 = service.compareTrainingHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTrainingHistory(entity1, entity2);
        const compareResult2 = service.compareTrainingHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
