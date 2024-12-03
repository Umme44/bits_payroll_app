import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserFeedback } from '../user-feedback.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-feedback.test-samples';

import { UserFeedbackService } from './user-feedback.service';

const requireRestSample: IUserFeedback = {
  ...sampleWithRequiredData,
};

describe('UserFeedback Service', () => {
  let service: UserFeedbackService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserFeedback | IUserFeedback[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserFeedbackService);
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

    it('should create a UserFeedback', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userFeedback = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userFeedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserFeedback', () => {
      const userFeedback = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userFeedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserFeedback', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserFeedback', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserFeedback', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserFeedbackToCollectionIfMissing', () => {
      it('should add a UserFeedback to an empty array', () => {
        const userFeedback: IUserFeedback = sampleWithRequiredData;
        expectedResult = service.addUserFeedbackToCollectionIfMissing([], userFeedback);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userFeedback);
      });

      it('should not add a UserFeedback to an array that contains it', () => {
        const userFeedback: IUserFeedback = sampleWithRequiredData;
        const userFeedbackCollection: IUserFeedback[] = [
          {
            ...userFeedback,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserFeedbackToCollectionIfMissing(userFeedbackCollection, userFeedback);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserFeedback to an array that doesn't contain it", () => {
        const userFeedback: IUserFeedback = sampleWithRequiredData;
        const userFeedbackCollection: IUserFeedback[] = [sampleWithPartialData];
        expectedResult = service.addUserFeedbackToCollectionIfMissing(userFeedbackCollection, userFeedback);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userFeedback);
      });

      it('should add only unique UserFeedback to an array', () => {
        const userFeedbackArray: IUserFeedback[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userFeedbackCollection: IUserFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addUserFeedbackToCollectionIfMissing(userFeedbackCollection, ...userFeedbackArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userFeedback: IUserFeedback = sampleWithRequiredData;
        const userFeedback2: IUserFeedback = sampleWithPartialData;
        expectedResult = service.addUserFeedbackToCollectionIfMissing([], userFeedback, userFeedback2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userFeedback);
        expect(expectedResult).toContain(userFeedback2);
      });

      it('should accept null and undefined values', () => {
        const userFeedback: IUserFeedback = sampleWithRequiredData;
        expectedResult = service.addUserFeedbackToCollectionIfMissing([], null, userFeedback, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userFeedback);
      });

      it('should return initial array if no UserFeedback is added', () => {
        const userFeedbackCollection: IUserFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addUserFeedbackToCollectionIfMissing(userFeedbackCollection, undefined, null);
        expect(expectedResult).toEqual(userFeedbackCollection);
      });
    });

    describe('compareUserFeedback', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserFeedback(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserFeedback(entity1, entity2);
        const compareResult2 = service.compareUserFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserFeedback(entity1, entity2);
        const compareResult2 = service.compareUserFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserFeedback(entity1, entity2);
        const compareResult2 = service.compareUserFeedback(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
