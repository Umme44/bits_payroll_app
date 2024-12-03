import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmployeeNOC } from '../employee-noc.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-noc.test-samples';

import { EmployeeNOCService, RestEmployeeNOC } from './employee-noc.service';

const requireRestSample: RestEmployeeNOC = {
  ...sampleWithRequiredData,
  leaveStartDate: sampleWithRequiredData.leaveStartDate?.format(DATE_FORMAT),
  leaveEndDate: sampleWithRequiredData.leaveEndDate?.format(DATE_FORMAT),
  issueDate: sampleWithRequiredData.issueDate?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  generatedAt: sampleWithRequiredData.generatedAt?.toJSON(),
};

describe('EmployeeNOC Service', () => {
  let service: EmployeeNOCService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeNOC | IEmployeeNOC[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeNOCService);
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

    it('should create a EmployeeNOC', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeNOC = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeNOC).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeNOC', () => {
      const employeeNOC = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeNOC).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeNOC', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeNOC', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeNOC', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeNOCToCollectionIfMissing', () => {
      it('should add a EmployeeNOC to an empty array', () => {
        const employeeNOC: IEmployeeNOC = sampleWithRequiredData;
        expectedResult = service.addEmployeeNOCToCollectionIfMissing([], employeeNOC);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeNOC);
      });

      it('should not add a EmployeeNOC to an array that contains it', () => {
        const employeeNOC: IEmployeeNOC = sampleWithRequiredData;
        const employeeNOCCollection: IEmployeeNOC[] = [
          {
            ...employeeNOC,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeNOCToCollectionIfMissing(employeeNOCCollection, employeeNOC);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeNOC to an array that doesn't contain it", () => {
        const employeeNOC: IEmployeeNOC = sampleWithRequiredData;
        const employeeNOCCollection: IEmployeeNOC[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeNOCToCollectionIfMissing(employeeNOCCollection, employeeNOC);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeNOC);
      });

      it('should add only unique EmployeeNOC to an array', () => {
        const employeeNOCArray: IEmployeeNOC[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeNOCCollection: IEmployeeNOC[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeNOCToCollectionIfMissing(employeeNOCCollection, ...employeeNOCArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeNOC: IEmployeeNOC = sampleWithRequiredData;
        const employeeNOC2: IEmployeeNOC = sampleWithPartialData;
        expectedResult = service.addEmployeeNOCToCollectionIfMissing([], employeeNOC, employeeNOC2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeNOC);
        expect(expectedResult).toContain(employeeNOC2);
      });

      it('should accept null and undefined values', () => {
        const employeeNOC: IEmployeeNOC = sampleWithRequiredData;
        expectedResult = service.addEmployeeNOCToCollectionIfMissing([], null, employeeNOC, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeNOC);
      });

      it('should return initial array if no EmployeeNOC is added', () => {
        const employeeNOCCollection: IEmployeeNOC[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeNOCToCollectionIfMissing(employeeNOCCollection, undefined, null);
        expect(expectedResult).toEqual(employeeNOCCollection);
      });
    });

    describe('compareEmployeeNOC', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeNOC(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeNOC(entity1, entity2);
        const compareResult2 = service.compareEmployeeNOC(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeNOC(entity1, entity2);
        const compareResult2 = service.compareEmployeeNOC(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeNOC(entity1, entity2);
        const compareResult2 = service.compareEmployeeNOC(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
