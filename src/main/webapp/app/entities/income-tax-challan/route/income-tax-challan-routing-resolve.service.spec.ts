import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { IncomeTaxChallanService } from '../service/income-tax-challan.service';

import { IncomeTaxChallanRoutingResolveService } from './income-tax-challan-routing-resolve.service';

describe('IncomeTaxChallan routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IncomeTaxChallanRoutingResolveService;
  let service: IncomeTaxChallanService;
  let resultIncomeTaxChallan: IIncomeTaxChallan | null | undefined;

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
    routingResolveService = TestBed.inject(IncomeTaxChallanRoutingResolveService);
    service = TestBed.inject(IncomeTaxChallanService);
    resultIncomeTaxChallan = undefined;
  });

  describe('resolve', () => {
    it('should return IIncomeTaxChallan returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeTaxChallan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIncomeTaxChallan).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeTaxChallan = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIncomeTaxChallan).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IIncomeTaxChallan>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeTaxChallan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIncomeTaxChallan).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
