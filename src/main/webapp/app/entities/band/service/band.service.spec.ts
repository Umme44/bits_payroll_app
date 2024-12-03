import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBand } from '../band.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../band.test-samples';

import { BandService, RestBand } from './band.service';

const requireRestSample: RestBand = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  updatedAt: sampleWithRequiredData.updatedAt?.format(DATE_FORMAT),
};

describe('Band Service', () => {
  let service: BandService;
  let httpMock: HttpTestingController;
  let expectedResult: IBand | IBand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BandService);
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

    it('should create a Band', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const band = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(band).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Band', () => {
      const band = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(band).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Band', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Band', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Band', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBandToCollectionIfMissing', () => {
      it('should add a Band to an empty array', () => {
        const band: IBand = sampleWithRequiredData;
        expectedResult = service.addBandToCollectionIfMissing([], band);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(band);
      });

      it('should not add a Band to an array that contains it', () => {
        const band: IBand = sampleWithRequiredData;
        const bandCollection: IBand[] = [
          {
            ...band,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBandToCollectionIfMissing(bandCollection, band);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Band to an array that doesn't contain it", () => {
        const band: IBand = sampleWithRequiredData;
        const bandCollection: IBand[] = [sampleWithPartialData];
        expectedResult = service.addBandToCollectionIfMissing(bandCollection, band);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(band);
      });

      it('should add only unique Band to an array', () => {
        const bandArray: IBand[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bandCollection: IBand[] = [sampleWithRequiredData];
        expectedResult = service.addBandToCollectionIfMissing(bandCollection, ...bandArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const band: IBand = sampleWithRequiredData;
        const band2: IBand = sampleWithPartialData;
        expectedResult = service.addBandToCollectionIfMissing([], band, band2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(band);
        expect(expectedResult).toContain(band2);
      });

      it('should accept null and undefined values', () => {
        const band: IBand = sampleWithRequiredData;
        expectedResult = service.addBandToCollectionIfMissing([], null, band, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(band);
      });

      it('should return initial array if no Band is added', () => {
        const bandCollection: IBand[] = [sampleWithRequiredData];
        expectedResult = service.addBandToCollectionIfMissing(bandCollection, undefined, null);
        expect(expectedResult).toEqual(bandCollection);
      });
    });

    describe('compareBand', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBand(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBand(entity1, entity2);
        const compareResult2 = service.compareBand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBand(entity1, entity2);
        const compareResult2 = service.compareBand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBand(entity1, entity2);
        const compareResult2 = service.compareBand(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
