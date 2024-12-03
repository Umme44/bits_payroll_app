import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';

import { PfLoanRepaymentRoutingResolveService } from './pf-loan-repayment-routing-resolve.service';

describe('PfLoanRepayment routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PfLoanRepaymentRoutingResolveService;
  let service: PfLoanRepaymentService;
  let resultPfLoanRepayment: IPfLoanRepayment | null | undefined;

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
    routingResolveService = TestBed.inject(PfLoanRepaymentRoutingResolveService);
    service = TestBed.inject(PfLoanRepaymentService);
    resultPfLoanRepayment = undefined;
  });

  describe('resolve', () => {
    it('should return IPfLoanRepayment returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanRepayment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPfLoanRepayment).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanRepayment = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPfLoanRepayment).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPfLoanRepayment>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanRepayment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPfLoanRepayment).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
