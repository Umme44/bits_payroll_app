import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILeaveApplication } from '../leave-application.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../leave-application.test-samples';

import { LeaveApplicationService, RestLeaveApplication } from './leave-application.service';

const requireRestSample: RestLeaveApplication = {
  ...sampleWithRequiredData,
  applicationDate: sampleWithRequiredData.applicationDate?.format(DATE_FORMAT),
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  sanctionedAt: sampleWithRequiredData.sanctionedAt?.toJSON(),
};

describe('LeaveApplication Service', () => {
  let service: LeaveApplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: ILeaveApplication | ILeaveApplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaveApplicationService);
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

    it('should create a LeaveApplication', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const leaveApplication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(leaveApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaveApplication', () => {
      const leaveApplication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(leaveApplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaveApplication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaveApplication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LeaveApplication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLeaveApplicationToCollectionIfMissing', () => {
      it('should add a LeaveApplication to an empty array', () => {
        const leaveApplication: ILeaveApplication = sampleWithRequiredData;
        expectedResult = service.addLeaveApplicationToCollectionIfMissing([], leaveApplication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveApplication);
      });

      it('should not add a LeaveApplication to an array that contains it', () => {
        const leaveApplication: ILeaveApplication = sampleWithRequiredData;
        const leaveApplicationCollection: ILeaveApplication[] = [
          {
            ...leaveApplication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, leaveApplication);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaveApplication to an array that doesn't contain it", () => {
        const leaveApplication: ILeaveApplication = sampleWithRequiredData;
        const leaveApplicationCollection: ILeaveApplication[] = [sampleWithPartialData];
        expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, leaveApplication);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveApplication);
      });

      it('should add only unique LeaveApplication to an array', () => {
        const leaveApplicationArray: ILeaveApplication[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const leaveApplicationCollection: ILeaveApplication[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, ...leaveApplicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaveApplication: ILeaveApplication = sampleWithRequiredData;
        const leaveApplication2: ILeaveApplication = sampleWithPartialData;
        expectedResult = service.addLeaveApplicationToCollectionIfMissing([], leaveApplication, leaveApplication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveApplication);
        expect(expectedResult).toContain(leaveApplication2);
      });

      it('should accept null and undefined values', () => {
        const leaveApplication: ILeaveApplication = sampleWithRequiredData;
        expectedResult = service.addLeaveApplicationToCollectionIfMissing([], null, leaveApplication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveApplication);
      });

      it('should return initial array if no LeaveApplication is added', () => {
        const leaveApplicationCollection: ILeaveApplication[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, undefined, null);
        expect(expectedResult).toEqual(leaveApplicationCollection);
      });
    });

    describe('compareLeaveApplication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLeaveApplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLeaveApplication(entity1, entity2);
        const compareResult2 = service.compareLeaveApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLeaveApplication(entity1, entity2);
        const compareResult2 = service.compareLeaveApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLeaveApplication(entity1, entity2);
        const compareResult2 = service.compareLeaveApplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
