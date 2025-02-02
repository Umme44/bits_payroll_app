import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArrearSalaryMaster } from '../arrear-salary-master.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../arrear-salary-master.test-samples';

import { ArrearSalaryMasterService } from './arrear-salary-master.service';

const requireRestSample: IArrearSalaryMaster = {
  ...sampleWithRequiredData,
};

describe('ArrearSalaryMaster Service', () => {
  let service: ArrearSalaryMasterService;
  let httpMock: HttpTestingController;
  let expectedResult: IArrearSalaryMaster | IArrearSalaryMaster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArrearSalaryMasterService);
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

    it('should create a ArrearSalaryMaster', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const arrearSalaryMaster = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(arrearSalaryMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArrearSalaryMaster', () => {
      const arrearSalaryMaster = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(arrearSalaryMaster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArrearSalaryMaster', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArrearSalaryMaster', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArrearSalaryMaster', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArrearSalaryMasterToCollectionIfMissing', () => {
      it('should add a ArrearSalaryMaster to an empty array', () => {
        const arrearSalaryMaster: IArrearSalaryMaster = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing([], arrearSalaryMaster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalaryMaster);
      });

      it('should not add a ArrearSalaryMaster to an array that contains it', () => {
        const arrearSalaryMaster: IArrearSalaryMaster = sampleWithRequiredData;
        const arrearSalaryMasterCollection: IArrearSalaryMaster[] = [
          {
            ...arrearSalaryMaster,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing(arrearSalaryMasterCollection, arrearSalaryMaster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArrearSalaryMaster to an array that doesn't contain it", () => {
        const arrearSalaryMaster: IArrearSalaryMaster = sampleWithRequiredData;
        const arrearSalaryMasterCollection: IArrearSalaryMaster[] = [sampleWithPartialData];
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing(arrearSalaryMasterCollection, arrearSalaryMaster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalaryMaster);
      });

      it('should add only unique ArrearSalaryMaster to an array', () => {
        const arrearSalaryMasterArray: IArrearSalaryMaster[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const arrearSalaryMasterCollection: IArrearSalaryMaster[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing(arrearSalaryMasterCollection, ...arrearSalaryMasterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arrearSalaryMaster: IArrearSalaryMaster = sampleWithRequiredData;
        const arrearSalaryMaster2: IArrearSalaryMaster = sampleWithPartialData;
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing([], arrearSalaryMaster, arrearSalaryMaster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arrearSalaryMaster);
        expect(expectedResult).toContain(arrearSalaryMaster2);
      });

      it('should accept null and undefined values', () => {
        const arrearSalaryMaster: IArrearSalaryMaster = sampleWithRequiredData;
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing([], null, arrearSalaryMaster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arrearSalaryMaster);
      });

      it('should return initial array if no ArrearSalaryMaster is added', () => {
        const arrearSalaryMasterCollection: IArrearSalaryMaster[] = [sampleWithRequiredData];
        expectedResult = service.addArrearSalaryMasterToCollectionIfMissing(arrearSalaryMasterCollection, undefined, null);
        expect(expectedResult).toEqual(arrearSalaryMasterCollection);
      });
    });

    describe('compareArrearSalaryMaster', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArrearSalaryMaster(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareArrearSalaryMaster(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareArrearSalaryMaster(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryMaster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareArrearSalaryMaster(entity1, entity2);
        const compareResult2 = service.compareArrearSalaryMaster(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
