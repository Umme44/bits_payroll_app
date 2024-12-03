import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPfLoanApplication } from '../pf-loan-application.model';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';

import { PfLoanApplicationRoutingResolveService } from './pf-loan-application-routing-resolve.service';

describe('PfLoanApplication routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PfLoanApplicationRoutingResolveService;
  let service: PfLoanApplicationService;
  let resultPfLoanApplication: IPfLoanApplication | null | undefined;

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
    routingResolveService = TestBed.inject(PfLoanApplicationRoutingResolveService);
    service = TestBed.inject(PfLoanApplicationService);
    resultPfLoanApplication = undefined;
  });

  describe('resolve', () => {
    it('should return IPfLoanApplication returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPfLoanApplication).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanApplication = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPfLoanApplication).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPfLoanApplication>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPfLoanApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPfLoanApplication).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
