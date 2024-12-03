import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOfficeNotices } from '../office-notices.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../office-notices.test-samples';

import { OfficeNoticesService, RestOfficeNotices } from './office-notices.service';

const requireRestSample: RestOfficeNotices = {
  ...sampleWithRequiredData,
  publishForm: sampleWithRequiredData.publishForm?.format(DATE_FORMAT),
  publishTo: sampleWithRequiredData.publishTo?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
};

describe('OfficeNotices Service', () => {
  let service: OfficeNoticesService;
  let httpMock: HttpTestingController;
  let expectedResult: IOfficeNotices | IOfficeNotices[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OfficeNoticesService);
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

    it('should create a OfficeNotices', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const officeNotices = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(officeNotices).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OfficeNotices', () => {
      const officeNotices = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(officeNotices).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OfficeNotices', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OfficeNotices', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OfficeNotices', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOfficeNoticesToCollectionIfMissing', () => {
      it('should add a OfficeNotices to an empty array', () => {
        const officeNotices: IOfficeNotices = sampleWithRequiredData;
        expectedResult = service.addOfficeNoticesToCollectionIfMissing([], officeNotices);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(officeNotices);
      });

      it('should not add a OfficeNotices to an array that contains it', () => {
        const officeNotices: IOfficeNotices = sampleWithRequiredData;
        const officeNoticesCollection: IOfficeNotices[] = [
          {
            ...officeNotices,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOfficeNoticesToCollectionIfMissing(officeNoticesCollection, officeNotices);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OfficeNotices to an array that doesn't contain it", () => {
        const officeNotices: IOfficeNotices = sampleWithRequiredData;
        const officeNoticesCollection: IOfficeNotices[] = [sampleWithPartialData];
        expectedResult = service.addOfficeNoticesToCollectionIfMissing(officeNoticesCollection, officeNotices);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(officeNotices);
      });

      it('should add only unique OfficeNotices to an array', () => {
        const officeNoticesArray: IOfficeNotices[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const officeNoticesCollection: IOfficeNotices[] = [sampleWithRequiredData];
        expectedResult = service.addOfficeNoticesToCollectionIfMissing(officeNoticesCollection, ...officeNoticesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const officeNotices: IOfficeNotices = sampleWithRequiredData;
        const officeNotices2: IOfficeNotices = sampleWithPartialData;
        expectedResult = service.addOfficeNoticesToCollectionIfMissing([], officeNotices, officeNotices2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(officeNotices);
        expect(expectedResult).toContain(officeNotices2);
      });

      it('should accept null and undefined values', () => {
        const officeNotices: IOfficeNotices = sampleWithRequiredData;
        expectedResult = service.addOfficeNoticesToCollectionIfMissing([], null, officeNotices, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(officeNotices);
      });

      it('should return initial array if no OfficeNotices is added', () => {
        const officeNoticesCollection: IOfficeNotices[] = [sampleWithRequiredData];
        expectedResult = service.addOfficeNoticesToCollectionIfMissing(officeNoticesCollection, undefined, null);
        expect(expectedResult).toEqual(officeNoticesCollection);
      });
    });

    describe('compareOfficeNotices', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOfficeNotices(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOfficeNotices(entity1, entity2);
        const compareResult2 = service.compareOfficeNotices(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOfficeNotices(entity1, entity2);
        const compareResult2 = service.compareOfficeNotices(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOfficeNotices(entity1, entity2);
        const compareResult2 = service.compareOfficeNotices(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
