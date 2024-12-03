import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInsuranceRegistration } from '../insurance-registration.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../insurance-registration.test-samples';

import { InsuranceRegistrationService, RestInsuranceRegistration } from './insurance-registration.service';

const requireRestSample: RestInsuranceRegistration = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  approvedAt: sampleWithRequiredData.approvedAt?.toJSON(),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('InsuranceRegistration Service', () => {
  let service: InsuranceRegistrationService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsuranceRegistration | IInsuranceRegistration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InsuranceRegistrationService);
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

    it('should create a InsuranceRegistration', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const insuranceRegistration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insuranceRegistration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsuranceRegistration', () => {
      const insuranceRegistration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insuranceRegistration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsuranceRegistration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsuranceRegistration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsuranceRegistration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInsuranceRegistrationToCollectionIfMissing', () => {
      it('should add a InsuranceRegistration to an empty array', () => {
        const insuranceRegistration: IInsuranceRegistration = sampleWithRequiredData;
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing([], insuranceRegistration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceRegistration);
      });

      it('should not add a InsuranceRegistration to an array that contains it', () => {
        const insuranceRegistration: IInsuranceRegistration = sampleWithRequiredData;
        const insuranceRegistrationCollection: IInsuranceRegistration[] = [
          {
            ...insuranceRegistration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing(insuranceRegistrationCollection, insuranceRegistration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsuranceRegistration to an array that doesn't contain it", () => {
        const insuranceRegistration: IInsuranceRegistration = sampleWithRequiredData;
        const insuranceRegistrationCollection: IInsuranceRegistration[] = [sampleWithPartialData];
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing(insuranceRegistrationCollection, insuranceRegistration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceRegistration);
      });

      it('should add only unique InsuranceRegistration to an array', () => {
        const insuranceRegistrationArray: IInsuranceRegistration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insuranceRegistrationCollection: IInsuranceRegistration[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing(
          insuranceRegistrationCollection,
          ...insuranceRegistrationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insuranceRegistration: IInsuranceRegistration = sampleWithRequiredData;
        const insuranceRegistration2: IInsuranceRegistration = sampleWithPartialData;
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing([], insuranceRegistration, insuranceRegistration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceRegistration);
        expect(expectedResult).toContain(insuranceRegistration2);
      });

      it('should accept null and undefined values', () => {
        const insuranceRegistration: IInsuranceRegistration = sampleWithRequiredData;
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing([], null, insuranceRegistration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceRegistration);
      });

      it('should return initial array if no InsuranceRegistration is added', () => {
        const insuranceRegistrationCollection: IInsuranceRegistration[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceRegistrationToCollectionIfMissing(insuranceRegistrationCollection, undefined, null);
        expect(expectedResult).toEqual(insuranceRegistrationCollection);
      });
    });

    describe('compareInsuranceRegistration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsuranceRegistration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInsuranceRegistration(entity1, entity2);
        const compareResult2 = service.compareInsuranceRegistration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInsuranceRegistration(entity1, entity2);
        const compareResult2 = service.compareInsuranceRegistration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInsuranceRegistration(entity1, entity2);
        const compareResult2 = service.compareInsuranceRegistration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
