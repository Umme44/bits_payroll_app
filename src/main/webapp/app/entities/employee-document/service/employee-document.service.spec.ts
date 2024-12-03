import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeDocument } from '../employee-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-document.test-samples';

import { EmployeeDocumentService, RestEmployeeDocument } from './employee-document.service';

const requireRestSample: RestEmployeeDocument = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('EmployeeDocument Service', () => {
  let service: EmployeeDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeDocument | IEmployeeDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeDocumentService);
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

    it('should create a EmployeeDocument', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeDocument', () => {
      const employeeDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeDocumentToCollectionIfMissing', () => {
      it('should add a EmployeeDocument to an empty array', () => {
        const employeeDocument: IEmployeeDocument = sampleWithRequiredData;
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing([], employeeDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeDocument);
      });

      it('should not add a EmployeeDocument to an array that contains it', () => {
        const employeeDocument: IEmployeeDocument = sampleWithRequiredData;
        const employeeDocumentCollection: IEmployeeDocument[] = [
          {
            ...employeeDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing(employeeDocumentCollection, employeeDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeDocument to an array that doesn't contain it", () => {
        const employeeDocument: IEmployeeDocument = sampleWithRequiredData;
        const employeeDocumentCollection: IEmployeeDocument[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing(employeeDocumentCollection, employeeDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeDocument);
      });

      it('should add only unique EmployeeDocument to an array', () => {
        const employeeDocumentArray: IEmployeeDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeDocumentCollection: IEmployeeDocument[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing(employeeDocumentCollection, ...employeeDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeDocument: IEmployeeDocument = sampleWithRequiredData;
        const employeeDocument2: IEmployeeDocument = sampleWithPartialData;
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing([], employeeDocument, employeeDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeDocument);
        expect(expectedResult).toContain(employeeDocument2);
      });

      it('should accept null and undefined values', () => {
        const employeeDocument: IEmployeeDocument = sampleWithRequiredData;
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing([], null, employeeDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeDocument);
      });

      it('should return initial array if no EmployeeDocument is added', () => {
        const employeeDocumentCollection: IEmployeeDocument[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeDocumentToCollectionIfMissing(employeeDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(employeeDocumentCollection);
      });
    });

    describe('compareEmployeeDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeDocument(entity1, entity2);
        const compareResult2 = service.compareEmployeeDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeDocument(entity1, entity2);
        const compareResult2 = service.compareEmployeeDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeDocument(entity1, entity2);
        const compareResult2 = service.compareEmployeeDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
