import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeaveAllocation } from '../leave-allocation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../leave-allocation.test-samples';

import { LeaveAllocationService } from './leave-allocation.service';

const requireRestSample: ILeaveAllocation = {
  ...sampleWithRequiredData,
};

describe('LeaveAllocation Service', () => {
  let service: LeaveAllocationService;
  let httpMock: HttpTestingController;
  let expectedResult: ILeaveAllocation | ILeaveAllocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaveAllocationService);
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

    it('should create a LeaveAllocation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const leaveAllocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(leaveAllocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaveAllocation', () => {
      const leaveAllocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(leaveAllocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaveAllocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaveAllocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LeaveAllocation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLeaveAllocationToCollectionIfMissing', () => {
      it('should add a LeaveAllocation to an empty array', () => {
        const leaveAllocation: ILeaveAllocation = sampleWithRequiredData;
        expectedResult = service.addLeaveAllocationToCollectionIfMissing([], leaveAllocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveAllocation);
      });

      it('should not add a LeaveAllocation to an array that contains it', () => {
        const leaveAllocation: ILeaveAllocation = sampleWithRequiredData;
        const leaveAllocationCollection: ILeaveAllocation[] = [
          {
            ...leaveAllocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLeaveAllocationToCollectionIfMissing(leaveAllocationCollection, leaveAllocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaveAllocation to an array that doesn't contain it", () => {
        const leaveAllocation: ILeaveAllocation = sampleWithRequiredData;
        const leaveAllocationCollection: ILeaveAllocation[] = [sampleWithPartialData];
        expectedResult = service.addLeaveAllocationToCollectionIfMissing(leaveAllocationCollection, leaveAllocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveAllocation);
      });

      it('should add only unique LeaveAllocation to an array', () => {
        const leaveAllocationArray: ILeaveAllocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const leaveAllocationCollection: ILeaveAllocation[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveAllocationToCollectionIfMissing(leaveAllocationCollection, ...leaveAllocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaveAllocation: ILeaveAllocation = sampleWithRequiredData;
        const leaveAllocation2: ILeaveAllocation = sampleWithPartialData;
        expectedResult = service.addLeaveAllocationToCollectionIfMissing([], leaveAllocation, leaveAllocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveAllocation);
        expect(expectedResult).toContain(leaveAllocation2);
      });

      it('should accept null and undefined values', () => {
        const leaveAllocation: ILeaveAllocation = sampleWithRequiredData;
        expectedResult = service.addLeaveAllocationToCollectionIfMissing([], null, leaveAllocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveAllocation);
      });

      it('should return initial array if no LeaveAllocation is added', () => {
        const leaveAllocationCollection: ILeaveAllocation[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveAllocationToCollectionIfMissing(leaveAllocationCollection, undefined, null);
        expect(expectedResult).toEqual(leaveAllocationCollection);
      });
    });

    describe('compareLeaveAllocation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLeaveAllocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLeaveAllocation(entity1, entity2);
        const compareResult2 = service.compareLeaveAllocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLeaveAllocation(entity1, entity2);
        const compareResult2 = service.compareLeaveAllocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLeaveAllocation(entity1, entity2);
        const compareResult2 = service.compareLeaveAllocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
