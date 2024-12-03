import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPfNominee } from '../pf-nominee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pf-nominee.test-samples';

import { PfNomineeService, RestPfNominee } from './pf-nominee.service';

const requireRestSample: RestPfNominee = {
  ...sampleWithRequiredData,
  nominationDate: sampleWithRequiredData.nominationDate?.format(DATE_FORMAT),
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  guardianDateOfBirth: sampleWithRequiredData.guardianDateOfBirth?.format(DATE_FORMAT),
};

describe('PfNominee Service', () => {
  let service: PfNomineeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPfNominee | IPfNominee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PfNomineeService);
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

    it('should create a PfNominee', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pfNominee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pfNominee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PfNominee', () => {
      const pfNominee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pfNominee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PfNominee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PfNominee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PfNominee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPfNomineeToCollectionIfMissing', () => {
      it('should add a PfNominee to an empty array', () => {
        const pfNominee: IPfNominee = sampleWithRequiredData;
        expectedResult = service.addPfNomineeToCollectionIfMissing([], pfNominee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfNominee);
      });

      it('should not add a PfNominee to an array that contains it', () => {
        const pfNominee: IPfNominee = sampleWithRequiredData;
        const pfNomineeCollection: IPfNominee[] = [
          {
            ...pfNominee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPfNomineeToCollectionIfMissing(pfNomineeCollection, pfNominee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PfNominee to an array that doesn't contain it", () => {
        const pfNominee: IPfNominee = sampleWithRequiredData;
        const pfNomineeCollection: IPfNominee[] = [sampleWithPartialData];
        expectedResult = service.addPfNomineeToCollectionIfMissing(pfNomineeCollection, pfNominee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfNominee);
      });

      it('should add only unique PfNominee to an array', () => {
        const pfNomineeArray: IPfNominee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pfNomineeCollection: IPfNominee[] = [sampleWithRequiredData];
        expectedResult = service.addPfNomineeToCollectionIfMissing(pfNomineeCollection, ...pfNomineeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pfNominee: IPfNominee = sampleWithRequiredData;
        const pfNominee2: IPfNominee = sampleWithPartialData;
        expectedResult = service.addPfNomineeToCollectionIfMissing([], pfNominee, pfNominee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pfNominee);
        expect(expectedResult).toContain(pfNominee2);
      });

      it('should accept null and undefined values', () => {
        const pfNominee: IPfNominee = sampleWithRequiredData;
        expectedResult = service.addPfNomineeToCollectionIfMissing([], null, pfNominee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pfNominee);
      });

      it('should return initial array if no PfNominee is added', () => {
        const pfNomineeCollection: IPfNominee[] = [sampleWithRequiredData];
        expectedResult = service.addPfNomineeToCollectionIfMissing(pfNomineeCollection, undefined, null);
        expect(expectedResult).toEqual(pfNomineeCollection);
      });
    });

    describe('comparePfNominee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePfNominee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePfNominee(entity1, entity2);
        const compareResult2 = service.comparePfNominee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePfNominee(entity1, entity2);
        const compareResult2 = service.comparePfNominee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePfNominee(entity1, entity2);
        const compareResult2 = service.comparePfNominee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
