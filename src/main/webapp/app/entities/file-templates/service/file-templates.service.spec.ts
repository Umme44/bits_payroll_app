import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFileTemplates } from '../file-templates.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../file-templates.test-samples';

import { FileTemplatesService } from './file-templates.service';

const requireRestSample: IFileTemplates = {
  ...sampleWithRequiredData,
};

describe('FileTemplates Service', () => {
  let service: FileTemplatesService;
  let httpMock: HttpTestingController;
  let expectedResult: IFileTemplates | IFileTemplates[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileTemplatesService);
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

    it('should create a FileTemplates', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fileTemplates = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fileTemplates).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileTemplates', () => {
      const fileTemplates = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fileTemplates).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileTemplates', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileTemplates', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FileTemplates', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFileTemplatesToCollectionIfMissing', () => {
      it('should add a FileTemplates to an empty array', () => {
        const fileTemplates: IFileTemplates = sampleWithRequiredData;
        expectedResult = service.addFileTemplatesToCollectionIfMissing([], fileTemplates);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileTemplates);
      });

      it('should not add a FileTemplates to an array that contains it', () => {
        const fileTemplates: IFileTemplates = sampleWithRequiredData;
        const fileTemplatesCollection: IFileTemplates[] = [
          {
            ...fileTemplates,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFileTemplatesToCollectionIfMissing(fileTemplatesCollection, fileTemplates);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileTemplates to an array that doesn't contain it", () => {
        const fileTemplates: IFileTemplates = sampleWithRequiredData;
        const fileTemplatesCollection: IFileTemplates[] = [sampleWithPartialData];
        expectedResult = service.addFileTemplatesToCollectionIfMissing(fileTemplatesCollection, fileTemplates);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileTemplates);
      });

      it('should add only unique FileTemplates to an array', () => {
        const fileTemplatesArray: IFileTemplates[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fileTemplatesCollection: IFileTemplates[] = [sampleWithRequiredData];
        expectedResult = service.addFileTemplatesToCollectionIfMissing(fileTemplatesCollection, ...fileTemplatesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileTemplates: IFileTemplates = sampleWithRequiredData;
        const fileTemplates2: IFileTemplates = sampleWithPartialData;
        expectedResult = service.addFileTemplatesToCollectionIfMissing([], fileTemplates, fileTemplates2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileTemplates);
        expect(expectedResult).toContain(fileTemplates2);
      });

      it('should accept null and undefined values', () => {
        const fileTemplates: IFileTemplates = sampleWithRequiredData;
        expectedResult = service.addFileTemplatesToCollectionIfMissing([], null, fileTemplates, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileTemplates);
      });

      it('should return initial array if no FileTemplates is added', () => {
        const fileTemplatesCollection: IFileTemplates[] = [sampleWithRequiredData];
        expectedResult = service.addFileTemplatesToCollectionIfMissing(fileTemplatesCollection, undefined, null);
        expect(expectedResult).toEqual(fileTemplatesCollection);
      });
    });

    describe('compareFileTemplates', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFileTemplates(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFileTemplates(entity1, entity2);
        const compareResult2 = service.compareFileTemplates(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFileTemplates(entity1, entity2);
        const compareResult2 = service.compareFileTemplates(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFileTemplates(entity1, entity2);
        const compareResult2 = service.compareFileTemplates(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
