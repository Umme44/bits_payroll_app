import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../employee-salary-temp-data.test-samples';

import { EmployeeSalaryTempDataService } from './employee-salary-temp-data.service';

const requireRestSample: IEmployeeSalaryTempData = {
  ...sampleWithRequiredData,
};

describe('EmployeeSalaryTempData Service', () => {
  let service: EmployeeSalaryTempDataService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeSalaryTempData | IEmployeeSalaryTempData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeSalaryTempDataService);
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

    it('should create a EmployeeSalaryTempData', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeSalaryTempData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeSalaryTempData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeSalaryTempData', () => {
      const employeeSalaryTempData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeSalaryTempData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeSalaryTempData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeSalaryTempData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeSalaryTempData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeSalaryTempDataToCollectionIfMissing', () => {
      it('should add a EmployeeSalaryTempData to an empty array', () => {
        const employeeSalaryTempData: IEmployeeSalaryTempData = sampleWithRequiredData;
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing([], employeeSalaryTempData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeSalaryTempData);
      });

      it('should not add a EmployeeSalaryTempData to an array that contains it', () => {
        const employeeSalaryTempData: IEmployeeSalaryTempData = sampleWithRequiredData;
        const employeeSalaryTempDataCollection: IEmployeeSalaryTempData[] = [
          {
            ...employeeSalaryTempData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing(employeeSalaryTempDataCollection, employeeSalaryTempData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeSalaryTempData to an array that doesn't contain it", () => {
        const employeeSalaryTempData: IEmployeeSalaryTempData = sampleWithRequiredData;
        const employeeSalaryTempDataCollection: IEmployeeSalaryTempData[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing(employeeSalaryTempDataCollection, employeeSalaryTempData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeSalaryTempData);
      });

      it('should add only unique EmployeeSalaryTempData to an array', () => {
        const employeeSalaryTempDataArray: IEmployeeSalaryTempData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeSalaryTempDataCollection: IEmployeeSalaryTempData[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing(
          employeeSalaryTempDataCollection,
          ...employeeSalaryTempDataArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeSalaryTempData: IEmployeeSalaryTempData = sampleWithRequiredData;
        const employeeSalaryTempData2: IEmployeeSalaryTempData = sampleWithPartialData;
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing([], employeeSalaryTempData, employeeSalaryTempData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeSalaryTempData);
        expect(expectedResult).toContain(employeeSalaryTempData2);
      });

      it('should accept null and undefined values', () => {
        const employeeSalaryTempData: IEmployeeSalaryTempData = sampleWithRequiredData;
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing([], null, employeeSalaryTempData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeSalaryTempData);
      });

      it('should return initial array if no EmployeeSalaryTempData is added', () => {
        const employeeSalaryTempDataCollection: IEmployeeSalaryTempData[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeSalaryTempDataToCollectionIfMissing(employeeSalaryTempDataCollection, undefined, null);
        expect(expectedResult).toEqual(employeeSalaryTempDataCollection);
      });
    });

    describe('compareEmployeeSalaryTempData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeSalaryTempData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeSalaryTempData(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalaryTempData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeSalaryTempData(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalaryTempData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeSalaryTempData(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalaryTempData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
