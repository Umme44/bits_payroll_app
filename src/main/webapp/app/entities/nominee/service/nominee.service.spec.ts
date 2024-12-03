import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INominee } from '../nominee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../nominee.test-samples';

import { NomineeService, RestNominee } from './nominee.service';

const requireRestSample: RestNominee = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  guardianDateOfBirth: sampleWithRequiredData.guardianDateOfBirth?.format(DATE_FORMAT),
  nominationDate: sampleWithRequiredData.nominationDate?.format(DATE_FORMAT),
};

describe('Nominee Service', () => {
  let service: NomineeService;
  let httpMock: HttpTestingController;
  let expectedResult: INominee | INominee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NomineeService);
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

    it('should create a Nominee', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const nominee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(nominee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Nominee', () => {
      const nominee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(nominee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Nominee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Nominee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Nominee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNomineeToCollectionIfMissing', () => {
      it('should add a Nominee to an empty array', () => {
        const nominee: INominee = sampleWithRequiredData;
        expectedResult = service.addNomineeToCollectionIfMissing([], nominee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nominee);
      });

      it('should not add a Nominee to an array that contains it', () => {
        const nominee: INominee = sampleWithRequiredData;
        const nomineeCollection: INominee[] = [
          {
            ...nominee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNomineeToCollectionIfMissing(nomineeCollection, nominee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Nominee to an array that doesn't contain it", () => {
        const nominee: INominee = sampleWithRequiredData;
        const nomineeCollection: INominee[] = [sampleWithPartialData];
        expectedResult = service.addNomineeToCollectionIfMissing(nomineeCollection, nominee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nominee);
      });

      it('should add only unique Nominee to an array', () => {
        const nomineeArray: INominee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const nomineeCollection: INominee[] = [sampleWithRequiredData];
        expectedResult = service.addNomineeToCollectionIfMissing(nomineeCollection, ...nomineeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nominee: INominee = sampleWithRequiredData;
        const nominee2: INominee = sampleWithPartialData;
        expectedResult = service.addNomineeToCollectionIfMissing([], nominee, nominee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nominee);
        expect(expectedResult).toContain(nominee2);
      });

      it('should accept null and undefined values', () => {
        const nominee: INominee = sampleWithRequiredData;
        expectedResult = service.addNomineeToCollectionIfMissing([], null, nominee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nominee);
      });

      it('should return initial array if no Nominee is added', () => {
        const nomineeCollection: INominee[] = [sampleWithRequiredData];
        expectedResult = service.addNomineeToCollectionIfMissing(nomineeCollection, undefined, null);
        expect(expectedResult).toEqual(nomineeCollection);
      });
    });

    describe('compareNominee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNominee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNominee(entity1, entity2);
        const compareResult2 = service.compareNominee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNominee(entity1, entity2);
        const compareResult2 = service.compareNominee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNominee(entity1, entity2);
        const compareResult2 = service.compareNominee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
