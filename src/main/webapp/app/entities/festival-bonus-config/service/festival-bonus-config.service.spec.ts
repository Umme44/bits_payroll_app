import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFestivalBonusConfig } from '../festival-bonus-config.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../festival-bonus-config.test-samples';

import { FestivalBonusConfigService } from './festival-bonus-config.service';

const requireRestSample: IFestivalBonusConfig = {
  ...sampleWithRequiredData,
};

describe('FestivalBonusConfig Service', () => {
  let service: FestivalBonusConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: IFestivalBonusConfig | IFestivalBonusConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FestivalBonusConfigService);
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

    it('should create a FestivalBonusConfig', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const festivalBonusConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(festivalBonusConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FestivalBonusConfig', () => {
      const festivalBonusConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(festivalBonusConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FestivalBonusConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FestivalBonusConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FestivalBonusConfig', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFestivalBonusConfigToCollectionIfMissing', () => {
      it('should add a FestivalBonusConfig to an empty array', () => {
        const festivalBonusConfig: IFestivalBonusConfig = sampleWithRequiredData;
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing([], festivalBonusConfig);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festivalBonusConfig);
      });

      it('should not add a FestivalBonusConfig to an array that contains it', () => {
        const festivalBonusConfig: IFestivalBonusConfig = sampleWithRequiredData;
        const festivalBonusConfigCollection: IFestivalBonusConfig[] = [
          {
            ...festivalBonusConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing(festivalBonusConfigCollection, festivalBonusConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FestivalBonusConfig to an array that doesn't contain it", () => {
        const festivalBonusConfig: IFestivalBonusConfig = sampleWithRequiredData;
        const festivalBonusConfigCollection: IFestivalBonusConfig[] = [sampleWithPartialData];
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing(festivalBonusConfigCollection, festivalBonusConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festivalBonusConfig);
      });

      it('should add only unique FestivalBonusConfig to an array', () => {
        const festivalBonusConfigArray: IFestivalBonusConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const festivalBonusConfigCollection: IFestivalBonusConfig[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing(festivalBonusConfigCollection, ...festivalBonusConfigArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const festivalBonusConfig: IFestivalBonusConfig = sampleWithRequiredData;
        const festivalBonusConfig2: IFestivalBonusConfig = sampleWithPartialData;
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing([], festivalBonusConfig, festivalBonusConfig2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(festivalBonusConfig);
        expect(expectedResult).toContain(festivalBonusConfig2);
      });

      it('should accept null and undefined values', () => {
        const festivalBonusConfig: IFestivalBonusConfig = sampleWithRequiredData;
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing([], null, festivalBonusConfig, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(festivalBonusConfig);
      });

      it('should return initial array if no FestivalBonusConfig is added', () => {
        const festivalBonusConfigCollection: IFestivalBonusConfig[] = [sampleWithRequiredData];
        expectedResult = service.addFestivalBonusConfigToCollectionIfMissing(festivalBonusConfigCollection, undefined, null);
        expect(expectedResult).toEqual(festivalBonusConfigCollection);
      });
    });

    describe('compareFestivalBonusConfig', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFestivalBonusConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFestivalBonusConfig(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFestivalBonusConfig(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFestivalBonusConfig(entity1, entity2);
        const compareResult2 = service.compareFestivalBonusConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
