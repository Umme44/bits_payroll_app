import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWorkFromHomeApplication } from '../work-from-home-application.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../work-from-home-application.test-samples';

import { WorkFromHomeApplicationService, RestWorkFromHomeApplication } from './work-from-home-application.service';

const requireRestSample: RestWorkFromHomeApplication = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  appliedAt: sampleWithRequiredData.appliedAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  sanctionedAt: sampleWithRequiredData.sanctionedAt?.toJSON(),
};

describe('WorkFromHomeApplication Service', () => {
  let service: WorkFromHomeApplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkFromHomeApplication | IWorkFromHomeApplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkFromHomeApplicationService);
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

    it('should create a WorkFromHomeApplication', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const workFromHomeApplication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workFromHomeApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkFromHomeApplication', () => {
      const workFromHomeApplication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workFromHomeApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkFromHomeApplication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkFromHomeApplication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkFromHomeApplication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkFromHomeApplicationToCollectionIfMissing', () => {
      it('should add a WorkFromHomeApplication to an empty array', () => {
        const workFromHomeApplication: IWorkFromHomeApplication = sampleWithRequiredData;
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing([], workFromHomeApplication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workFromHomeApplication);
      });

      it('should not add a WorkFromHomeApplication to an array that contains it', () => {
        const workFromHomeApplication: IWorkFromHomeApplication = sampleWithRequiredData;
        const workFromHomeApplicationCollection: IWorkFromHomeApplication[] = [
          {
            ...workFromHomeApplication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing(
          workFromHomeApplicationCollection,
          workFromHomeApplication
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkFromHomeApplication to an array that doesn't contain it", () => {
        const workFromHomeApplication: IWorkFromHomeApplication = sampleWithRequiredData;
        const workFromHomeApplicationCollection: IWorkFromHomeApplication[] = [sampleWithPartialData];
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing(
          workFromHomeApplicationCollection,
          workFromHomeApplication
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workFromHomeApplication);
      });

      it('should add only unique WorkFromHomeApplication to an array', () => {
        const workFromHomeApplicationArray: IWorkFromHomeApplication[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const workFromHomeApplicationCollection: IWorkFromHomeApplication[] = [sampleWithRequiredData];
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing(
          workFromHomeApplicationCollection,
          ...workFromHomeApplicationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workFromHomeApplication: IWorkFromHomeApplication = sampleWithRequiredData;
        const workFromHomeApplication2: IWorkFromHomeApplication = sampleWithPartialData;
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing([], workFromHomeApplication, workFromHomeApplication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workFromHomeApplication);
        expect(expectedResult).toContain(workFromHomeApplication2);
      });

      it('should accept null and undefined values', () => {
        const workFromHomeApplication: IWorkFromHomeApplication = sampleWithRequiredData;
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing([], null, workFromHomeApplication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workFromHomeApplication);
      });

      it('should return initial array if no WorkFromHomeApplication is added', () => {
        const workFromHomeApplicationCollection: IWorkFromHomeApplication[] = [sampleWithRequiredData];
        expectedResult = service.addWorkFromHomeApplicationToCollectionIfMissing(workFromHomeApplicationCollection, undefined, null);
        expect(expectedResult).toEqual(workFromHomeApplicationCollection);
      });
    });

    describe('compareWorkFromHomeApplication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkFromHomeApplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkFromHomeApplication(entity1, entity2);
        const compareResult2 = service.compareWorkFromHomeApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkFromHomeApplication(entity1, entity2);
        const compareResult2 = service.compareWorkFromHomeApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkFromHomeApplication(entity1, entity2);
        const compareResult2 = service.compareWorkFromHomeApplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
