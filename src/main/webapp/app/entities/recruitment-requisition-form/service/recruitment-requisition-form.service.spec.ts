import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../recruitment-requisition-form.test-samples';

import { RecruitmentRequisitionFormService, RestRecruitmentRequisitionForm } from './recruitment-requisition-form.service';

const requireRestSample: RestRecruitmentRequisitionForm = {
  ...sampleWithRequiredData,
  expectedJoiningDate: sampleWithRequiredData.expectedJoiningDate?.format(DATE_FORMAT),
  dateOfRequisition: sampleWithRequiredData.dateOfRequisition?.format(DATE_FORMAT),
  requestedDate: sampleWithRequiredData.requestedDate?.format(DATE_FORMAT),
  recommendationDate01: sampleWithRequiredData.recommendationDate01?.format(DATE_FORMAT),
  recommendationDate02: sampleWithRequiredData.recommendationDate02?.format(DATE_FORMAT),
  recommendationDate03: sampleWithRequiredData.recommendationDate03?.format(DATE_FORMAT),
  recommendationDate04: sampleWithRequiredData.recommendationDate04?.format(DATE_FORMAT),
  rejectedAt: sampleWithRequiredData.rejectedAt?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
  recommendationDate05: sampleWithRequiredData.recommendationDate05?.format(DATE_FORMAT),
};

describe('RecruitmentRequisitionForm Service', () => {
  let service: RecruitmentRequisitionFormService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecruitmentRequisitionForm | IRecruitmentRequisitionForm[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecruitmentRequisitionFormService);
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

    it('should create a RecruitmentRequisitionForm', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const recruitmentRequisitionForm = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recruitmentRequisitionForm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecruitmentRequisitionForm', () => {
      const recruitmentRequisitionForm = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recruitmentRequisitionForm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecruitmentRequisitionForm', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecruitmentRequisitionForm', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RecruitmentRequisitionForm', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecruitmentRequisitionFormToCollectionIfMissing', () => {
      it('should add a RecruitmentRequisitionForm to an empty array', () => {
        const recruitmentRequisitionForm: IRecruitmentRequisitionForm = sampleWithRequiredData;
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing([], recruitmentRequisitionForm);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruitmentRequisitionForm);
      });

      it('should not add a RecruitmentRequisitionForm to an array that contains it', () => {
        const recruitmentRequisitionForm: IRecruitmentRequisitionForm = sampleWithRequiredData;
        const recruitmentRequisitionFormCollection: IRecruitmentRequisitionForm[] = [
          {
            ...recruitmentRequisitionForm,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing(
          recruitmentRequisitionFormCollection,
          recruitmentRequisitionForm
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecruitmentRequisitionForm to an array that doesn't contain it", () => {
        const recruitmentRequisitionForm: IRecruitmentRequisitionForm = sampleWithRequiredData;
        const recruitmentRequisitionFormCollection: IRecruitmentRequisitionForm[] = [sampleWithPartialData];
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing(
          recruitmentRequisitionFormCollection,
          recruitmentRequisitionForm
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruitmentRequisitionForm);
      });

      it('should add only unique RecruitmentRequisitionForm to an array', () => {
        const recruitmentRequisitionFormArray: IRecruitmentRequisitionForm[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const recruitmentRequisitionFormCollection: IRecruitmentRequisitionForm[] = [sampleWithRequiredData];
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing(
          recruitmentRequisitionFormCollection,
          ...recruitmentRequisitionFormArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recruitmentRequisitionForm: IRecruitmentRequisitionForm = sampleWithRequiredData;
        const recruitmentRequisitionForm2: IRecruitmentRequisitionForm = sampleWithPartialData;
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing(
          [],
          recruitmentRequisitionForm,
          recruitmentRequisitionForm2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruitmentRequisitionForm);
        expect(expectedResult).toContain(recruitmentRequisitionForm2);
      });

      it('should accept null and undefined values', () => {
        const recruitmentRequisitionForm: IRecruitmentRequisitionForm = sampleWithRequiredData;
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing([], null, recruitmentRequisitionForm, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruitmentRequisitionForm);
      });

      it('should return initial array if no RecruitmentRequisitionForm is added', () => {
        const recruitmentRequisitionFormCollection: IRecruitmentRequisitionForm[] = [sampleWithRequiredData];
        expectedResult = service.addRecruitmentRequisitionFormToCollectionIfMissing(recruitmentRequisitionFormCollection, undefined, null);
        expect(expectedResult).toEqual(recruitmentRequisitionFormCollection);
      });
    });

    describe('compareRecruitmentRequisitionForm', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecruitmentRequisitionForm(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRecruitmentRequisitionForm(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionForm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRecruitmentRequisitionForm(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionForm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRecruitmentRequisitionForm(entity1, entity2);
        const compareResult2 = service.compareRecruitmentRequisitionForm(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
