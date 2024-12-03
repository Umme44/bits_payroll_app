import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventLog } from '../event-log.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../event-log.test-samples';

import { EventLogService, RestEventLog } from './event-log.service';

const requireRestSample: RestEventLog = {
  ...sampleWithRequiredData,
  performedAt: sampleWithRequiredData.performedAt?.toJSON(),
};

describe('EventLog Service', () => {
  let service: EventLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IEventLog | IEventLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventLogService);
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

    it('should create a EventLog', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eventLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eventLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventLog', () => {
      const eventLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eventLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EventLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEventLogToCollectionIfMissing', () => {
      it('should add a EventLog to an empty array', () => {
        const eventLog: IEventLog = sampleWithRequiredData;
        expectedResult = service.addEventLogToCollectionIfMissing([], eventLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLog);
      });

      it('should not add a EventLog to an array that contains it', () => {
        const eventLog: IEventLog = sampleWithRequiredData;
        const eventLogCollection: IEventLog[] = [
          {
            ...eventLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEventLogToCollectionIfMissing(eventLogCollection, eventLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventLog to an array that doesn't contain it", () => {
        const eventLog: IEventLog = sampleWithRequiredData;
        const eventLogCollection: IEventLog[] = [sampleWithPartialData];
        expectedResult = service.addEventLogToCollectionIfMissing(eventLogCollection, eventLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLog);
      });

      it('should add only unique EventLog to an array', () => {
        const eventLogArray: IEventLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eventLogCollection: IEventLog[] = [sampleWithRequiredData];
        expectedResult = service.addEventLogToCollectionIfMissing(eventLogCollection, ...eventLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventLog: IEventLog = sampleWithRequiredData;
        const eventLog2: IEventLog = sampleWithPartialData;
        expectedResult = service.addEventLogToCollectionIfMissing([], eventLog, eventLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLog);
        expect(expectedResult).toContain(eventLog2);
      });

      it('should accept null and undefined values', () => {
        const eventLog: IEventLog = sampleWithRequiredData;
        expectedResult = service.addEventLogToCollectionIfMissing([], null, eventLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLog);
      });

      it('should return initial array if no EventLog is added', () => {
        const eventLogCollection: IEventLog[] = [sampleWithRequiredData];
        expectedResult = service.addEventLogToCollectionIfMissing(eventLogCollection, undefined, null);
        expect(expectedResult).toEqual(eventLogCollection);
      });
    });

    describe('compareEventLog', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEventLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEventLog(entity1, entity2);
        const compareResult2 = service.compareEventLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEventLog(entity1, entity2);
        const compareResult2 = service.compareEventLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEventLog(entity1, entity2);
        const compareResult2 = service.compareEventLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
