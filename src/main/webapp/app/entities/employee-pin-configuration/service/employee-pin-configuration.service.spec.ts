import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../employee-pin-configuration.test-samples';

import { EmployeePinConfigurationService, RestEmployeePinConfiguration } from './employee-pin-configuration.service';

const requireRestSample: RestEmployeePinConfiguration = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('EmployeePinConfiguration Service', () => {
  let service: EmployeePinConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeePinConfiguration | IEmployeePinConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeePinConfigurationService);
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

    it('should create a EmployeePinConfiguration', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeePinConfiguration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeePinConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeePinConfiguration', () => {
      const employeePinConfiguration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeePinConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeePinConfiguration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeePinConfiguration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeePinConfiguration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeePinConfigurationToCollectionIfMissing', () => {
      it('should add a EmployeePinConfiguration to an empty array', () => {
        const employeePinConfiguration: IEmployeePinConfiguration = sampleWithRequiredData;
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing([], employeePinConfiguration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeePinConfiguration);
      });

      it('should not add a EmployeePinConfiguration to an array that contains it', () => {
        const employeePinConfiguration: IEmployeePinConfiguration = sampleWithRequiredData;
        const employeePinConfigurationCollection: IEmployeePinConfiguration[] = [
          {
            ...employeePinConfiguration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing(
          employeePinConfigurationCollection,
          employeePinConfiguration
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeePinConfiguration to an array that doesn't contain it", () => {
        const employeePinConfiguration: IEmployeePinConfiguration = sampleWithRequiredData;
        const employeePinConfigurationCollection: IEmployeePinConfiguration[] = [sampleWithPartialData];
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing(
          employeePinConfigurationCollection,
          employeePinConfiguration
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeePinConfiguration);
      });

      it('should add only unique EmployeePinConfiguration to an array', () => {
        const employeePinConfigurationArray: IEmployeePinConfiguration[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const employeePinConfigurationCollection: IEmployeePinConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing(
          employeePinConfigurationCollection,
          ...employeePinConfigurationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeePinConfiguration: IEmployeePinConfiguration = sampleWithRequiredData;
        const employeePinConfiguration2: IEmployeePinConfiguration = sampleWithPartialData;
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing([], employeePinConfiguration, employeePinConfiguration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeePinConfiguration);
        expect(expectedResult).toContain(employeePinConfiguration2);
      });

      it('should accept null and undefined values', () => {
        const employeePinConfiguration: IEmployeePinConfiguration = sampleWithRequiredData;
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing([], null, employeePinConfiguration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeePinConfiguration);
      });

      it('should return initial array if no EmployeePinConfiguration is added', () => {
        const employeePinConfigurationCollection: IEmployeePinConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeePinConfigurationToCollectionIfMissing(employeePinConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(employeePinConfigurationCollection);
      });
    });

    describe('compareEmployeePinConfiguration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeePinConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeePinConfiguration(entity1, entity2);
        const compareResult2 = service.compareEmployeePinConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeePinConfiguration(entity1, entity2);
        const compareResult2 = service.compareEmployeePinConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeePinConfiguration(entity1, entity2);
        const compareResult2 = service.compareEmployeePinConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
