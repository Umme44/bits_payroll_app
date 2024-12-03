import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmployeeResignation } from '../employee-resignation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-resignation.test-samples';

import { EmployeeResignationService, RestEmployeeResignation } from './employee-resignation.service';

const requireRestSample: RestEmployeeResignation = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
  resignationDate: sampleWithRequiredData.resignationDate?.format(DATE_FORMAT),
  lastWorkingDay: sampleWithRequiredData.lastWorkingDay?.format(DATE_FORMAT),
};

describe('EmployeeResignation Service', () => {
  let service: EmployeeResignationService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeResignation | IEmployeeResignation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeResignationService);
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

    it('should create a EmployeeResignation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeResignation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeResignation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeResignation', () => {
      const employeeResignation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeResignation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeResignation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeResignation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeResignation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeResignationToCollectionIfMissing', () => {
      it('should add a EmployeeResignation to an empty array', () => {
        const employeeResignation: IEmployeeResignation = sampleWithRequiredData;
        expectedResult = service.addEmployeeResignationToCollectionIfMissing([], employeeResignation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeResignation);
      });

      it('should not add a EmployeeResignation to an array that contains it', () => {
        const employeeResignation: IEmployeeResignation = sampleWithRequiredData;
        const employeeResignationCollection: IEmployeeResignation[] = [
          {
            ...employeeResignation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeResignationToCollectionIfMissing(employeeResignationCollection, employeeResignation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeResignation to an array that doesn't contain it", () => {
        const employeeResignation: IEmployeeResignation = sampleWithRequiredData;
        const employeeResignationCollection: IEmployeeResignation[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeResignationToCollectionIfMissing(employeeResignationCollection, employeeResignation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeResignation);
      });

      it('should add only unique EmployeeResignation to an array', () => {
        const employeeResignationArray: IEmployeeResignation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeResignationCollection: IEmployeeResignation[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeResignationToCollectionIfMissing(employeeResignationCollection, ...employeeResignationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeResignation: IEmployeeResignation = sampleWithRequiredData;
        const employeeResignation2: IEmployeeResignation = sampleWithPartialData;
        expectedResult = service.addEmployeeResignationToCollectionIfMissing([], employeeResignation, employeeResignation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeResignation);
        expect(expectedResult).toContain(employeeResignation2);
      });

      it('should accept null and undefined values', () => {
        const employeeResignation: IEmployeeResignation = sampleWithRequiredData;
        expectedResult = service.addEmployeeResignationToCollectionIfMissing([], null, employeeResignation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeResignation);
      });

      it('should return initial array if no EmployeeResignation is added', () => {
        const employeeResignationCollection: IEmployeeResignation[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeResignationToCollectionIfMissing(employeeResignationCollection, undefined, null);
        expect(expectedResult).toEqual(employeeResignationCollection);
      });
    });

    describe('compareEmployeeResignation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeResignation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeResignation(entity1, entity2);
        const compareResult2 = service.compareEmployeeResignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeResignation(entity1, entity2);
        const compareResult2 = service.compareEmployeeResignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeResignation(entity1, entity2);
        const compareResult2 = service.compareEmployeeResignation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
