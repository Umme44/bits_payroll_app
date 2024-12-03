import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProcReq } from '../proc-req.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../proc-req.test-samples';

import { ProcReqService } from './proc-req.service';

const requireRestSample: IProcReq = {
  ...sampleWithRequiredData,
};

describe('ProcReq Service', () => {
  let service: ProcReqService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcReq | IProcReq[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProcReqService);
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

    it('should create a ProcReq', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const procReq = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(procReq).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProcReq', () => {
      const procReq = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(procReq).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProcReq', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProcReq', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProcReq', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProcReqToCollectionIfMissing', () => {
      it('should add a ProcReq to an empty array', () => {
        const procReq: IProcReq = sampleWithRequiredData;
        expectedResult = service.addProcReqToCollectionIfMissing([], procReq);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procReq);
      });

      it('should not add a ProcReq to an array that contains it', () => {
        const procReq: IProcReq = sampleWithRequiredData;
        const procReqCollection: IProcReq[] = [
          {
            ...procReq,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcReqToCollectionIfMissing(procReqCollection, procReq);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProcReq to an array that doesn't contain it", () => {
        const procReq: IProcReq = sampleWithRequiredData;
        const procReqCollection: IProcReq[] = [sampleWithPartialData];
        expectedResult = service.addProcReqToCollectionIfMissing(procReqCollection, procReq);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procReq);
      });

      it('should add only unique ProcReq to an array', () => {
        const procReqArray: IProcReq[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const procReqCollection: IProcReq[] = [sampleWithRequiredData];
        expectedResult = service.addProcReqToCollectionIfMissing(procReqCollection, ...procReqArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const procReq: IProcReq = sampleWithRequiredData;
        const procReq2: IProcReq = sampleWithPartialData;
        expectedResult = service.addProcReqToCollectionIfMissing([], procReq, procReq2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procReq);
        expect(expectedResult).toContain(procReq2);
      });

      it('should accept null and undefined values', () => {
        const procReq: IProcReq = sampleWithRequiredData;
        expectedResult = service.addProcReqToCollectionIfMissing([], null, procReq, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procReq);
      });

      it('should return initial array if no ProcReq is added', () => {
        const procReqCollection: IProcReq[] = [sampleWithRequiredData];
        expectedResult = service.addProcReqToCollectionIfMissing(procReqCollection, undefined, null);
        expect(expectedResult).toEqual(procReqCollection);
      });
    });

    describe('compareProcReq', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcReq(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcReq(entity1, entity2);
        const compareResult2 = service.compareProcReq(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcReq(entity1, entity2);
        const compareResult2 = service.compareProcReq(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcReq(entity1, entity2);
        const compareResult2 = service.compareProcReq(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
