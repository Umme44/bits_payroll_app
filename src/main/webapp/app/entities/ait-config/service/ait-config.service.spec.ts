import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAitConfig } from '../ait-config.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ait-config.test-samples';

import { AitConfigService, RestAitConfig } from './ait-config.service';

const requireRestSample: RestAitConfig = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('AitConfig Service', () => {
  let service: AitConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: IAitConfig | IAitConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AitConfigService);
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

    it('should create a AitConfig', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const aitConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aitConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AitConfig', () => {
      const aitConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aitConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AitConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AitConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AitConfig', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAitConfigToCollectionIfMissing', () => {
      it('should add a AitConfig to an empty array', () => {
        const aitConfig: IAitConfig = sampleWithRequiredData;
        expectedResult = service.addAitConfigToCollectionIfMissing([], aitConfig);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aitConfig);
      });

      it('should not add a AitConfig to an array that contains it', () => {
        const aitConfig: IAitConfig = sampleWithRequiredData;
        const aitConfigCollection: IAitConfig[] = [
          {
            ...aitConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAitConfigToCollectionIfMissing(aitConfigCollection, aitConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AitConfig to an array that doesn't contain it", () => {
        const aitConfig: IAitConfig = sampleWithRequiredData;
        const aitConfigCollection: IAitConfig[] = [sampleWithPartialData];
        expectedResult = service.addAitConfigToCollectionIfMissing(aitConfigCollection, aitConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aitConfig);
      });

      it('should add only unique AitConfig to an array', () => {
        const aitConfigArray: IAitConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aitConfigCollection: IAitConfig[] = [sampleWithRequiredData];
        expectedResult = service.addAitConfigToCollectionIfMissing(aitConfigCollection, ...aitConfigArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aitConfig: IAitConfig = sampleWithRequiredData;
        const aitConfig2: IAitConfig = sampleWithPartialData;
        expectedResult = service.addAitConfigToCollectionIfMissing([], aitConfig, aitConfig2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aitConfig);
        expect(expectedResult).toContain(aitConfig2);
      });

      it('should accept null and undefined values', () => {
        const aitConfig: IAitConfig = sampleWithRequiredData;
        expectedResult = service.addAitConfigToCollectionIfMissing([], null, aitConfig, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aitConfig);
      });

      it('should return initial array if no AitConfig is added', () => {
        const aitConfigCollection: IAitConfig[] = [sampleWithRequiredData];
        expectedResult = service.addAitConfigToCollectionIfMissing(aitConfigCollection, undefined, null);
        expect(expectedResult).toEqual(aitConfigCollection);
      });
    });

    describe('compareAitConfig', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAitConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAitConfig(entity1, entity2);
        const compareResult2 = service.compareAitConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAitConfig(entity1, entity2);
        const compareResult2 = service.compareAitConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAitConfig(entity1, entity2);
        const compareResult2 = service.compareAitConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
