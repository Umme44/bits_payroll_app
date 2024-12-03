import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWorkingExperience } from '../working-experience.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../working-experience.test-samples';

import { WorkingExperienceService, RestWorkingExperience } from './working-experience.service';

const requireRestSample: RestWorkingExperience = {
  ...sampleWithRequiredData,
  dojOfLastOrganization: sampleWithRequiredData.dojOfLastOrganization?.format(DATE_FORMAT),
  dorOfLastOrganization: sampleWithRequiredData.dorOfLastOrganization?.format(DATE_FORMAT),
};

describe('WorkingExperience Service', () => {
  let service: WorkingExperienceService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkingExperience | IWorkingExperience[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkingExperienceService);
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

    it('should create a WorkingExperience', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const workingExperience = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workingExperience).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkingExperience', () => {
      const workingExperience = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workingExperience).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkingExperience', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkingExperience', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkingExperience', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkingExperienceToCollectionIfMissing', () => {
      it('should add a WorkingExperience to an empty array', () => {
        const workingExperience: IWorkingExperience = sampleWithRequiredData;
        expectedResult = service.addWorkingExperienceToCollectionIfMissing([], workingExperience);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workingExperience);
      });

      it('should not add a WorkingExperience to an array that contains it', () => {
        const workingExperience: IWorkingExperience = sampleWithRequiredData;
        const workingExperienceCollection: IWorkingExperience[] = [
          {
            ...workingExperience,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkingExperienceToCollectionIfMissing(workingExperienceCollection, workingExperience);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkingExperience to an array that doesn't contain it", () => {
        const workingExperience: IWorkingExperience = sampleWithRequiredData;
        const workingExperienceCollection: IWorkingExperience[] = [sampleWithPartialData];
        expectedResult = service.addWorkingExperienceToCollectionIfMissing(workingExperienceCollection, workingExperience);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workingExperience);
      });

      it('should add only unique WorkingExperience to an array', () => {
        const workingExperienceArray: IWorkingExperience[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workingExperienceCollection: IWorkingExperience[] = [sampleWithRequiredData];
        expectedResult = service.addWorkingExperienceToCollectionIfMissing(workingExperienceCollection, ...workingExperienceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workingExperience: IWorkingExperience = sampleWithRequiredData;
        const workingExperience2: IWorkingExperience = sampleWithPartialData;
        expectedResult = service.addWorkingExperienceToCollectionIfMissing([], workingExperience, workingExperience2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workingExperience);
        expect(expectedResult).toContain(workingExperience2);
      });

      it('should accept null and undefined values', () => {
        const workingExperience: IWorkingExperience = sampleWithRequiredData;
        expectedResult = service.addWorkingExperienceToCollectionIfMissing([], null, workingExperience, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workingExperience);
      });

      it('should return initial array if no WorkingExperience is added', () => {
        const workingExperienceCollection: IWorkingExperience[] = [sampleWithRequiredData];
        expectedResult = service.addWorkingExperienceToCollectionIfMissing(workingExperienceCollection, undefined, null);
        expect(expectedResult).toEqual(workingExperienceCollection);
      });
    });

    describe('compareWorkingExperience', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkingExperience(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkingExperience(entity1, entity2);
        const compareResult2 = service.compareWorkingExperience(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkingExperience(entity1, entity2);
        const compareResult2 = service.compareWorkingExperience(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkingExperience(entity1, entity2);
        const compareResult2 = service.compareWorkingExperience(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
