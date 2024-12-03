import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISalaryGeneratorMaster } from '../salary-generator-master.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../salary-generator-master.test-samples';

import { SalaryGeneratorMasterService } from './salary-generator-master.service';

const requireRestSample: ISalaryGeneratorMaster = {
  ...sampleWithRequiredData,
};

describe('SalaryGeneratorMaster Service', () => {
  let service: SalaryGeneratorMasterService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalaryGeneratorMaster | ISalaryGeneratorMaster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryGeneratorMasterService);
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

    it('should create a SalaryGeneratorMaster', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const salaryGeneratorMaster = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salaryGeneratorMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalaryGeneratorMaster', () => {
      const salaryGeneratorMaster = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salaryGeneratorMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalaryGeneratorMaster', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalaryGeneratorMaster', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalaryGeneratorMaster', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalaryGeneratorMasterToCollectionIfMissing', () => {
      it('should add a SalaryGeneratorMaster to an empty array', () => {
        const salaryGeneratorMaster: ISalaryGeneratorMaster = sampleWithRequiredData;
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing([], salaryGeneratorMaster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryGeneratorMaster);
      });

      it('should not add a SalaryGeneratorMaster to an array that contains it', () => {
        const salaryGeneratorMaster: ISalaryGeneratorMaster = sampleWithRequiredData;
        const salaryGeneratorMasterCollection: ISalaryGeneratorMaster[] = [
          {
            ...salaryGeneratorMaster,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing(salaryGeneratorMasterCollection, salaryGeneratorMaster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalaryGeneratorMaster to an array that doesn't contain it", () => {
        const salaryGeneratorMaster: ISalaryGeneratorMaster = sampleWithRequiredData;
        const salaryGeneratorMasterCollection: ISalaryGeneratorMaster[] = [sampleWithPartialData];
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing(salaryGeneratorMasterCollection, salaryGeneratorMaster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryGeneratorMaster);
      });

      it('should add only unique SalaryGeneratorMaster to an array', () => {
        const salaryGeneratorMasterArray: ISalaryGeneratorMaster[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryGeneratorMasterCollection: ISalaryGeneratorMaster[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing(
          salaryGeneratorMasterCollection,
          ...salaryGeneratorMasterArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salaryGeneratorMaster: ISalaryGeneratorMaster = sampleWithRequiredData;
        const salaryGeneratorMaster2: ISalaryGeneratorMaster = sampleWithPartialData;
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing([], salaryGeneratorMaster, salaryGeneratorMaster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryGeneratorMaster);
        expect(expectedResult).toContain(salaryGeneratorMaster2);
      });

      it('should accept null and undefined values', () => {
        const salaryGeneratorMaster: ISalaryGeneratorMaster = sampleWithRequiredData;
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing([], null, salaryGeneratorMaster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryGeneratorMaster);
      });

      it('should return initial array if no SalaryGeneratorMaster is added', () => {
        const salaryGeneratorMasterCollection: ISalaryGeneratorMaster[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryGeneratorMasterToCollectionIfMissing(salaryGeneratorMasterCollection, undefined, null);
        expect(expectedResult).toEqual(salaryGeneratorMasterCollection);
      });
    });

    describe('compareSalaryGeneratorMaster', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalaryGeneratorMaster(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalaryGeneratorMaster(entity1, entity2);
        const compareResult2 = service.compareSalaryGeneratorMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalaryGeneratorMaster(entity1, entity2);
        const compareResult2 = service.compareSalaryGeneratorMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalaryGeneratorMaster(entity1, entity2);
        const compareResult2 = service.compareSalaryGeneratorMaster(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
