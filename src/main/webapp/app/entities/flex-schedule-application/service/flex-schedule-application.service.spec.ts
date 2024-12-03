import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFlexScheduleApplication } from '../flex-schedule-application.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../flex-schedule-application.test-samples';

import { FlexScheduleApplicationService, RestFlexScheduleApplication } from './flex-schedule-application.service';

const requireRestSample: RestFlexScheduleApplication = {
  ...sampleWithRequiredData,
  effectiveFrom: sampleWithRequiredData.effectiveFrom?.format(DATE_FORMAT),
  effectiveTo: sampleWithRequiredData.effectiveTo?.format(DATE_FORMAT),
  sanctionedAt: sampleWithRequiredData.sanctionedAt?.toJSON(),
  appliedAt: sampleWithRequiredData.appliedAt?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('FlexScheduleApplication Service', () => {
  let service: FlexScheduleApplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: IFlexScheduleApplication | IFlexScheduleApplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FlexScheduleApplicationService);
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

    it('should create a FlexScheduleApplication', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const flexScheduleApplication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(flexScheduleApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FlexScheduleApplication', () => {
      const flexScheduleApplication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(flexScheduleApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FlexScheduleApplication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FlexScheduleApplication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FlexScheduleApplication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFlexScheduleApplicationToCollectionIfMissing', () => {
      it('should add a FlexScheduleApplication to an empty array', () => {
        const flexScheduleApplication: IFlexScheduleApplication = sampleWithRequiredData;
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing([], flexScheduleApplication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flexScheduleApplication);
      });

      it('should not add a FlexScheduleApplication to an array that contains it', () => {
        const flexScheduleApplication: IFlexScheduleApplication = sampleWithRequiredData;
        const flexScheduleApplicationCollection: IFlexScheduleApplication[] = [
          {
            ...flexScheduleApplication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing(
          flexScheduleApplicationCollection,
          flexScheduleApplication
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FlexScheduleApplication to an array that doesn't contain it", () => {
        const flexScheduleApplication: IFlexScheduleApplication = sampleWithRequiredData;
        const flexScheduleApplicationCollection: IFlexScheduleApplication[] = [sampleWithPartialData];
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing(
          flexScheduleApplicationCollection,
          flexScheduleApplication
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flexScheduleApplication);
      });

      it('should add only unique FlexScheduleApplication to an array', () => {
        const flexScheduleApplicationArray: IFlexScheduleApplication[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const flexScheduleApplicationCollection: IFlexScheduleApplication[] = [sampleWithRequiredData];
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing(
          flexScheduleApplicationCollection,
          ...flexScheduleApplicationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const flexScheduleApplication: IFlexScheduleApplication = sampleWithRequiredData;
        const flexScheduleApplication2: IFlexScheduleApplication = sampleWithPartialData;
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing([], flexScheduleApplication, flexScheduleApplication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flexScheduleApplication);
        expect(expectedResult).toContain(flexScheduleApplication2);
      });

      it('should accept null and undefined values', () => {
        const flexScheduleApplication: IFlexScheduleApplication = sampleWithRequiredData;
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing([], null, flexScheduleApplication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flexScheduleApplication);
      });

      it('should return initial array if no FlexScheduleApplication is added', () => {
        const flexScheduleApplicationCollection: IFlexScheduleApplication[] = [sampleWithRequiredData];
        expectedResult = service.addFlexScheduleApplicationToCollectionIfMissing(flexScheduleApplicationCollection, undefined, null);
        expect(expectedResult).toEqual(flexScheduleApplicationCollection);
      });
    });

    describe('compareFlexScheduleApplication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFlexScheduleApplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFlexScheduleApplication(entity1, entity2);
        const compareResult2 = service.compareFlexScheduleApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFlexScheduleApplication(entity1, entity2);
        const compareResult2 = service.compareFlexScheduleApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFlexScheduleApplication(entity1, entity2);
        const compareResult2 = service.compareFlexScheduleApplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
