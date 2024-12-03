import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProcReqMaster } from '../proc-req-master.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../proc-req-master.test-samples';

import { ProcReqMasterService, RestProcReqMaster } from './proc-req-master.service';

const requireRestSample: RestProcReqMaster = {
  ...sampleWithRequiredData,
  requestedDate: sampleWithRequiredData.requestedDate?.format(DATE_FORMAT),
  expectedReceivedDate: sampleWithRequiredData.expectedReceivedDate?.format(DATE_FORMAT),
  recommendationAt01: sampleWithRequiredData.recommendationAt01?.toJSON(),
  recommendationAt02: sampleWithRequiredData.recommendationAt02?.toJSON(),
  recommendationAt03: sampleWithRequiredData.recommendationAt03?.toJSON(),
  recommendationAt04: sampleWithRequiredData.recommendationAt04?.toJSON(),
  recommendationAt05: sampleWithRequiredData.recommendationAt05?.toJSON(),
  rejectedDate: sampleWithRequiredData.rejectedDate?.format(DATE_FORMAT),
  closedAt: sampleWithRequiredData.closedAt?.toJSON(),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('ProcReqMaster Service', () => {
  let service: ProcReqMasterService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcReqMaster | IProcReqMaster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProcReqMasterService);
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

    it('should create a ProcReqMaster', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const procReqMaster = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(procReqMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProcReqMaster', () => {
      const procReqMaster = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(procReqMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProcReqMaster', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProcReqMaster', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProcReqMaster', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProcReqMasterToCollectionIfMissing', () => {
      it('should add a ProcReqMaster to an empty array', () => {
        const procReqMaster: IProcReqMaster = sampleWithRequiredData;
        expectedResult = service.addProcReqMasterToCollectionIfMissing([], procReqMaster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procReqMaster);
      });

      it('should not add a ProcReqMaster to an array that contains it', () => {
        const procReqMaster: IProcReqMaster = sampleWithRequiredData;
        const procReqMasterCollection: IProcReqMaster[] = [
          {
            ...procReqMaster,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcReqMasterToCollectionIfMissing(procReqMasterCollection, procReqMaster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProcReqMaster to an array that doesn't contain it", () => {
        const procReqMaster: IProcReqMaster = sampleWithRequiredData;
        const procReqMasterCollection: IProcReqMaster[] = [sampleWithPartialData];
        expectedResult = service.addProcReqMasterToCollectionIfMissing(procReqMasterCollection, procReqMaster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procReqMaster);
      });

      it('should add only unique ProcReqMaster to an array', () => {
        const procReqMasterArray: IProcReqMaster[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const procReqMasterCollection: IProcReqMaster[] = [sampleWithRequiredData];
        expectedResult = service.addProcReqMasterToCollectionIfMissing(procReqMasterCollection, ...procReqMasterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const procReqMaster: IProcReqMaster = sampleWithRequiredData;
        const procReqMaster2: IProcReqMaster = sampleWithPartialData;
        expectedResult = service.addProcReqMasterToCollectionIfMissing([], procReqMaster, procReqMaster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procReqMaster);
        expect(expectedResult).toContain(procReqMaster2);
      });

      it('should accept null and undefined values', () => {
        const procReqMaster: IProcReqMaster = sampleWithRequiredData;
        expectedResult = service.addProcReqMasterToCollectionIfMissing([], null, procReqMaster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procReqMaster);
      });

      it('should return initial array if no ProcReqMaster is added', () => {
        const procReqMasterCollection: IProcReqMaster[] = [sampleWithRequiredData];
        expectedResult = service.addProcReqMasterToCollectionIfMissing(procReqMasterCollection, undefined, null);
        expect(expectedResult).toEqual(procReqMasterCollection);
      });
    });

    describe('compareProcReqMaster', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcReqMaster(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcReqMaster(entity1, entity2);
        const compareResult2 = service.compareProcReqMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcReqMaster(entity1, entity2);
        const compareResult2 = service.compareProcReqMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcReqMaster(entity1, entity2);
        const compareResult2 = service.compareProcReqMaster(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
