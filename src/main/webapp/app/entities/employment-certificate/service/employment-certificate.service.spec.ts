import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmploymentCertificate } from '../employment-certificate.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../employment-certificate.test-samples';

import { EmploymentCertificateService, RestEmploymentCertificate } from './employment-certificate.service';

const requireRestSample: RestEmploymentCertificate = {
  ...sampleWithRequiredData,
  issueDate: sampleWithRequiredData.issueDate?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  generatedAt: sampleWithRequiredData.generatedAt?.toJSON(),
};

describe('EmploymentCertificate Service', () => {
  let service: EmploymentCertificateService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmploymentCertificate | IEmploymentCertificate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmploymentCertificateService);
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

    it('should create a EmploymentCertificate', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employmentCertificate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employmentCertificate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmploymentCertificate', () => {
      const employmentCertificate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employmentCertificate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmploymentCertificate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmploymentCertificate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmploymentCertificate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmploymentCertificateToCollectionIfMissing', () => {
      it('should add a EmploymentCertificate to an empty array', () => {
        const employmentCertificate: IEmploymentCertificate = sampleWithRequiredData;
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing([], employmentCertificate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentCertificate);
      });

      it('should not add a EmploymentCertificate to an array that contains it', () => {
        const employmentCertificate: IEmploymentCertificate = sampleWithRequiredData;
        const employmentCertificateCollection: IEmploymentCertificate[] = [
          {
            ...employmentCertificate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing(employmentCertificateCollection, employmentCertificate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmploymentCertificate to an array that doesn't contain it", () => {
        const employmentCertificate: IEmploymentCertificate = sampleWithRequiredData;
        const employmentCertificateCollection: IEmploymentCertificate[] = [sampleWithPartialData];
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing(employmentCertificateCollection, employmentCertificate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentCertificate);
      });

      it('should add only unique EmploymentCertificate to an array', () => {
        const employmentCertificateArray: IEmploymentCertificate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employmentCertificateCollection: IEmploymentCertificate[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing(
          employmentCertificateCollection,
          ...employmentCertificateArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employmentCertificate: IEmploymentCertificate = sampleWithRequiredData;
        const employmentCertificate2: IEmploymentCertificate = sampleWithPartialData;
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing([], employmentCertificate, employmentCertificate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentCertificate);
        expect(expectedResult).toContain(employmentCertificate2);
      });

      it('should accept null and undefined values', () => {
        const employmentCertificate: IEmploymentCertificate = sampleWithRequiredData;
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing([], null, employmentCertificate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentCertificate);
      });

      it('should return initial array if no EmploymentCertificate is added', () => {
        const employmentCertificateCollection: IEmploymentCertificate[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentCertificateToCollectionIfMissing(employmentCertificateCollection, undefined, null);
        expect(expectedResult).toEqual(employmentCertificateCollection);
      });
    });

    describe('compareEmploymentCertificate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmploymentCertificate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmploymentCertificate(entity1, entity2);
        const compareResult2 = service.compareEmploymentCertificate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmploymentCertificate(entity1, entity2);
        const compareResult2 = service.compareEmploymentCertificate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmploymentCertificate(entity1, entity2);
        const compareResult2 = service.compareEmploymentCertificate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
