import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';

import { UnitOfMeasurementRoutingResolveService } from './unit-of-measurement-routing-resolve.service';

describe('UnitOfMeasurement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UnitOfMeasurementRoutingResolveService;
  let service: UnitOfMeasurementService;
  let resultUnitOfMeasurement: IUnitOfMeasurement | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(UnitOfMeasurementRoutingResolveService);
    service = TestBed.inject(UnitOfMeasurementService);
    resultUnitOfMeasurement = undefined;
  });

  describe('resolve', () => {
    it('should return IUnitOfMeasurement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUnitOfMeasurement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUnitOfMeasurement).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUnitOfMeasurement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUnitOfMeasurement).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IUnitOfMeasurement>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUnitOfMeasurement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUnitOfMeasurement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
