import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInsuranceConfiguration } from '../insurance-configuration.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../insurance-configuration.test-samples';

import { InsuranceConfigurationService } from './insurance-configuration.service';

const requireRestSample: IInsuranceConfiguration = {
  ...sampleWithRequiredData,
};

describe('InsuranceConfiguration Service', () => {
  let service: InsuranceConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsuranceConfiguration | IInsuranceConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InsuranceConfigurationService);
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

    it('should create a InsuranceConfiguration', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const insuranceConfiguration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insuranceConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsuranceConfiguration', () => {
      const insuranceConfiguration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insuranceConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsuranceConfiguration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsuranceConfiguration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsuranceConfiguration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInsuranceConfigurationToCollectionIfMissing', () => {
      it('should add a InsuranceConfiguration to an empty array', () => {
        const insuranceConfiguration: IInsuranceConfiguration = sampleWithRequiredData;
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing([], insuranceConfiguration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceConfiguration);
      });

      it('should not add a InsuranceConfiguration to an array that contains it', () => {
        const insuranceConfiguration: IInsuranceConfiguration = sampleWithRequiredData;
        const insuranceConfigurationCollection: IInsuranceConfiguration[] = [
          {
            ...insuranceConfiguration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing(insuranceConfigurationCollection, insuranceConfiguration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsuranceConfiguration to an array that doesn't contain it", () => {
        const insuranceConfiguration: IInsuranceConfiguration = sampleWithRequiredData;
        const insuranceConfigurationCollection: IInsuranceConfiguration[] = [sampleWithPartialData];
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing(insuranceConfigurationCollection, insuranceConfiguration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceConfiguration);
      });

      it('should add only unique InsuranceConfiguration to an array', () => {
        const insuranceConfigurationArray: IInsuranceConfiguration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insuranceConfigurationCollection: IInsuranceConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing(
          insuranceConfigurationCollection,
          ...insuranceConfigurationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insuranceConfiguration: IInsuranceConfiguration = sampleWithRequiredData;
        const insuranceConfiguration2: IInsuranceConfiguration = sampleWithPartialData;
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing([], insuranceConfiguration, insuranceConfiguration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceConfiguration);
        expect(expectedResult).toContain(insuranceConfiguration2);
      });

      it('should accept null and undefined values', () => {
        const insuranceConfiguration: IInsuranceConfiguration = sampleWithRequiredData;
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing([], null, insuranceConfiguration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceConfiguration);
      });

      it('should return initial array if no InsuranceConfiguration is added', () => {
        const insuranceConfigurationCollection: IInsuranceConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceConfigurationToCollectionIfMissing(insuranceConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(insuranceConfigurationCollection);
      });
    });

    describe('compareInsuranceConfiguration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsuranceConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInsuranceConfiguration(entity1, entity2);
        const compareResult2 = service.compareInsuranceConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInsuranceConfiguration(entity1, entity2);
        const compareResult2 = service.compareInsuranceConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInsuranceConfiguration(entity1, entity2);
        const compareResult2 = service.compareInsuranceConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
