import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeePin } from '../employee-pin.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-pin.test-samples';

import { EmployeePinService, RestEmployeePin } from './employee-pin.service';

const requireRestSample: RestEmployeePin = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('EmployeePin Service', () => {
  let service: EmployeePinService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeePin | IEmployeePin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeePinService);
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

    it('should create a EmployeePin', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeePin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeePin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeePin', () => {
      const employeePin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeePin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeePin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeePin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeePin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeePinToCollectionIfMissing', () => {
      it('should add a EmployeePin to an empty array', () => {
        const employeePin: IEmployeePin = sampleWithRequiredData;
        expectedResult = service.addEmployeePinToCollectionIfMissing([], employeePin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeePin);
      });

      it('should not add a EmployeePin to an array that contains it', () => {
        const employeePin: IEmployeePin = sampleWithRequiredData;
        const employeePinCollection: IEmployeePin[] = [
          {
            ...employeePin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeePinToCollectionIfMissing(employeePinCollection, employeePin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeePin to an array that doesn't contain it", () => {
        const employeePin: IEmployeePin = sampleWithRequiredData;
        const employeePinCollection: IEmployeePin[] = [sampleWithPartialData];
        expectedResult = service.addEmployeePinToCollectionIfMissing(employeePinCollection, employeePin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeePin);
      });

      it('should add only unique EmployeePin to an array', () => {
        const employeePinArray: IEmployeePin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeePinCollection: IEmployeePin[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeePinToCollectionIfMissing(employeePinCollection, ...employeePinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeePin: IEmployeePin = sampleWithRequiredData;
        const employeePin2: IEmployeePin = sampleWithPartialData;
        expectedResult = service.addEmployeePinToCollectionIfMissing([], employeePin, employeePin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeePin);
        expect(expectedResult).toContain(employeePin2);
      });

      it('should accept null and undefined values', () => {
        const employeePin: IEmployeePin = sampleWithRequiredData;
        expectedResult = service.addEmployeePinToCollectionIfMissing([], null, employeePin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeePin);
      });

      it('should return initial array if no EmployeePin is added', () => {
        const employeePinCollection: IEmployeePin[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeePinToCollectionIfMissing(employeePinCollection, undefined, null);
        expect(expectedResult).toEqual(employeePinCollection);
      });
    });

    describe('compareEmployeePin', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeePin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeePin(entity1, entity2);
        const compareResult2 = service.compareEmployeePin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeePin(entity1, entity2);
        const compareResult2 = service.compareEmployeePin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeePin(entity1, entity2);
        const compareResult2 = service.compareEmployeePin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
