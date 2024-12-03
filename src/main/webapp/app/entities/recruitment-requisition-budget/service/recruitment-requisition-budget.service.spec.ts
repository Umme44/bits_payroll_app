import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../recruitment-requisition-budget.test-samples';

import { RecruitmentRequisitionBudgetService } from './recruitment-requisition-budget.service';

const requireRestSample: IRecruitmentRequisitionBudget = {
  ...sampleWithRequiredData,
};

describe('RecruitmentRequisitionBudget Service', () => {
  let service: RecruitmentRequisitionBudgetService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecruitmentRequisitionBudget | IRecruitmentRequisitionBudget[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecruitmentRequisitionBudgetService);
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

    it('should create a RecruitmentRequisitionBudget', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const recruitmentRequisitionBudget = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recruitmentRequisitionBudget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecruitmentRequisitionBudget', () => {
      const recruitmentRequisitionBudget = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recruitmentRequisitionBudget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecruitmentRequisitionBudget', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecruitmentRequisitionBudget', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RecruitmentRequisitionBudget', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecruitmentRequisitionBudgetToCollectionIfMissing', () => {
      it('should add a RecruitmentRequisitionBudget to an empty array', () => {
        const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = sampleWithRequiredData;
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing([], recruitmentRequisitionBudget);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruitmentRequisitionBudget);
      });

      it('should not add a RecruitmentRequisitionBudget to an array that contains it', () => {
        const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = sampleWithRequiredData;
        const recruitmentRequisitionBudgetCollection: IRecruitmentRequisitionBudget[] = [
          {
            ...recruitmentRequisitionBudget,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing(
          recruitmentRequisitionBudgetCollection,
          recruitmentRequisitionBudget
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecruitmentRequisitionBudget to an array that doesn't contain it", () => {
        const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = sampleWithRequiredData;
        const recruitmentRequisitionBudgetCollection: IRecruitmentRequisitionBudget[] = [sampleWithPartialData];
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing(
          recruitmentRequisitionBudgetCollection,
          recruitmentRequisitionBudget
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruitmentRequisitionBudget);
      });

      it('should add only unique RecruitmentRequisitionBudget to an array', () => {
        const recruitmentRequisitionBudgetArray: IRecruitmentRequisitionBudget[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const recruitmentRequisitionBudgetCollection: IRecruitmentRequisitionBudget[] = [sampleWithRequiredData];
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing(
          recruitmentRequisitionBudgetCollection,
          ...recruitmentRequisitionBudgetArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = sampleWithRequiredData;
        const recruitmentRequisitionBudget2: IRecruitmentRequisitionBudget = sampleWithPartialData;
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing(
          [],
          recruitmentRequisitionBudget,
          recruitmentRequisitionBudget2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruitmentRequisitionBudget);
        expect(expectedResult).toContain(recruitmentRequisitionBudget2);
      });

      it('should accept null and undefined values', () => {
        const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = sampleWithRequiredData;
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing([], null, recruitmentRequisitionBudget, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruitmentRequisitionBudget);
      });

      it('should return initial array if no RecruitmentRequisitionBudget is added', () => {
        const recruitmentRequisitionBudgetCollection: IRecruitmentRequisitionBudget[] = [sampleWithRequiredData];
        expectedResult = service.addRecruitmentRequisitionBudgetToCollectionIfMissing(
          recruitmentRequisitionBudgetCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(recruitmentRequisitionBudgetCollection);
      });
    });

    describe('compareRecruitmentRequisitionBudget', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecruitmentRequisitionBudget(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRecruitmentRequisitionBudget(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionBudget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRecruitmentRequisitionBudget(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionBudget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRecruitmentRequisitionBudget(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionBudget(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
