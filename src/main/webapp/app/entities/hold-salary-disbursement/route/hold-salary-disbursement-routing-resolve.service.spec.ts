import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';
import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';

import { HoldSalaryDisbursementRoutingResolveService } from './hold-salary-disbursement-routing-resolve.service';

describe('HoldSalaryDisbursement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HoldSalaryDisbursementRoutingResolveService;
  let service: HoldSalaryDisbursementService;
  let resultHoldSalaryDisbursement: IHoldSalaryDisbursement | null | undefined;

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
    routingResolveService = TestBed.inject(HoldSalaryDisbursementRoutingResolveService);
    service = TestBed.inject(HoldSalaryDisbursementService);
    resultHoldSalaryDisbursement = undefined;
  });

  describe('resolve', () => {
    it('should return IHoldSalaryDisbursement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldSalaryDisbursement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHoldSalaryDisbursement).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldSalaryDisbursement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHoldSalaryDisbursement).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IHoldSalaryDisbursement>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHoldSalaryDisbursement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHoldSalaryDisbursement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
