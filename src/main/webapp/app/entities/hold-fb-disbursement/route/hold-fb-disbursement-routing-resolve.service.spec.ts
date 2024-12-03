import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';

import { HoldFbDisbursementRoutingResolveService } from './hold-fb-disbursement-routing-resolve.service';

describe('HoldFbDisbursement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HoldFbDisbursementRoutingResolveService;
  let service: HoldFbDisbursementService;
  let resultHoldFbDisbursement: IHoldFbDisbursement | null | undefined;

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
    routingResolveService = TestBed.inject(HoldFbDisbursementRoutingResolveService);
    service = TestBed.inject(HoldFbDisbursementService);
    resultHoldFbDisbursement = undefined;
  });

  describe('resolve', () => {
    it('should return IHoldFbDisbursement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldFbDisbursement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHoldFbDisbursement).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldFbDisbursement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHoldFbDisbursement).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IHoldFbDisbursement>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldFbDisbursement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHoldFbDisbursement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
