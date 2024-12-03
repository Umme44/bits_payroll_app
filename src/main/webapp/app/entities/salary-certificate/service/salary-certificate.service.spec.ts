import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISalaryCertificate } from '../salary-certificate.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../salary-certificate.test-samples';

import { SalaryCertificateService, RestSalaryCertificate } from './salary-certificate.service';

const requireRestSample: RestSalaryCertificate = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
  sanctionAt: sampleWithRequiredData.sanctionAt?.format(DATE_FORMAT),
};

describe('SalaryCertificate Service', () => {
  let service: SalaryCertificateService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalaryCertificate | ISalaryCertificate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryCertificateService);
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

    it('should create a SalaryCertificate', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const salaryCertificate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salaryCertificate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalaryCertificate', () => {
      const salaryCertificate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salaryCertificate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalaryCertificate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalaryCertificate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalaryCertificate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalaryCertificateToCollectionIfMissing', () => {
      it('should add a SalaryCertificate to an empty array', () => {
        const salaryCertificate: ISalaryCertificate = sampleWithRequiredData;
        expectedResult = service.addSalaryCertificateToCollectionIfMissing([], salaryCertificate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryCertificate);
      });

      it('should not add a SalaryCertificate to an array that contains it', () => {
        const salaryCertificate: ISalaryCertificate = sampleWithRequiredData;
        const salaryCertificateCollection: ISalaryCertificate[] = [
          {
            ...salaryCertificate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryCertificateToCollectionIfMissing(salaryCertificateCollection, salaryCertificate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalaryCertificate to an array that doesn't contain it", () => {
        const salaryCertificate: ISalaryCertificate = sampleWithRequiredData;
        const salaryCertificateCollection: ISalaryCertificate[] = [sampleWithPartialData];
        expectedResult = service.addSalaryCertificateToCollectionIfMissing(salaryCertificateCollection, salaryCertificate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryCertificate);
      });

      it('should add only unique SalaryCertificate to an array', () => {
        const salaryCertificateArray: ISalaryCertificate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryCertificateCollection: ISalaryCertificate[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryCertificateToCollectionIfMissing(salaryCertificateCollection, ...salaryCertificateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salaryCertificate: ISalaryCertificate = sampleWithRequiredData;
        const salaryCertificate2: ISalaryCertificate = sampleWithPartialData;
        expectedResult = service.addSalaryCertificateToCollectionIfMissing([], salaryCertificate, salaryCertificate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryCertificate);
        expect(expectedResult).toContain(salaryCertificate2);
      });

      it('should accept null and undefined values', () => {
        const salaryCertificate: ISalaryCertificate = sampleWithRequiredData;
        expectedResult = service.addSalaryCertificateToCollectionIfMissing([], null, salaryCertificate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryCertificate);
      });

      it('should return initial array if no SalaryCertificate is added', () => {
        const salaryCertificateCollection: ISalaryCertificate[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryCertificateToCollectionIfMissing(salaryCertificateCollection, undefined, null);
        expect(expectedResult).toEqual(salaryCertificateCollection);
      });
    });

    describe('compareSalaryCertificate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalaryCertificate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalaryCertificate(entity1, entity2);
        const compareResult2 = service.compareSalaryCertificate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalaryCertificate(entity1, entity2);
        const compareResult2 = service.compareSalaryCertificate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalaryCertificate(entity1, entity2);
        const compareResult2 = service.compareSalaryCertificate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
