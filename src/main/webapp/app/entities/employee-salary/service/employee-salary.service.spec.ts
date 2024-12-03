import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmployeeSalary } from '../employee-salary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-salary.test-samples';

import { EmployeeSalaryService, RestEmployeeSalary } from './employee-salary.service';

const requireRestSample: RestEmployeeSalary = {
  ...sampleWithRequiredData,
  salaryGenerationDate: sampleWithRequiredData.salaryGenerationDate?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  joiningDate: sampleWithRequiredData.joiningDate?.format(DATE_FORMAT),
  confirmationDate: sampleWithRequiredData.confirmationDate?.format(DATE_FORMAT),
  attendanceRegularisationStartDate: sampleWithRequiredData.attendanceRegularisationStartDate?.format(DATE_FORMAT),
  attendanceRegularisationEndDate: sampleWithRequiredData.attendanceRegularisationEndDate?.format(DATE_FORMAT),
};

describe('EmployeeSalary Service', () => {
  let service: EmployeeSalaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeSalary | IEmployeeSalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeSalaryService);
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

    it('should create a EmployeeSalary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeSalary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeSalary', () => {
      const employeeSalary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeSalary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeSalary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeSalary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeSalaryToCollectionIfMissing', () => {
      it('should add a EmployeeSalary to an empty array', () => {
        const employeeSalary: IEmployeeSalary = sampleWithRequiredData;
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing([], employeeSalary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeSalary);
      });

      it('should not add a EmployeeSalary to an array that contains it', () => {
        const employeeSalary: IEmployeeSalary = sampleWithRequiredData;
        const employeeSalaryCollection: IEmployeeSalary[] = [
          {
            ...employeeSalary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing(employeeSalaryCollection, employeeSalary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeSalary to an array that doesn't contain it", () => {
        const employeeSalary: IEmployeeSalary = sampleWithRequiredData;
        const employeeSalaryCollection: IEmployeeSalary[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing(employeeSalaryCollection, employeeSalary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeSalary);
      });

      it('should add only unique EmployeeSalary to an array', () => {
        const employeeSalaryArray: IEmployeeSalary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeSalaryCollection: IEmployeeSalary[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing(employeeSalaryCollection, ...employeeSalaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeSalary: IEmployeeSalary = sampleWithRequiredData;
        const employeeSalary2: IEmployeeSalary = sampleWithPartialData;
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing([], employeeSalary, employeeSalary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeSalary);
        expect(expectedResult).toContain(employeeSalary2);
      });

      it('should accept null and undefined values', () => {
        const employeeSalary: IEmployeeSalary = sampleWithRequiredData;
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing([], null, employeeSalary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeSalary);
      });

      it('should return initial array if no EmployeeSalary is added', () => {
        const employeeSalaryCollection: IEmployeeSalary[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeSalaryToCollectionIfMissing(employeeSalaryCollection, undefined, null);
        expect(expectedResult).toEqual(employeeSalaryCollection);
      });
    });

    describe('compareEmployeeSalary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeSalary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
