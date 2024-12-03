import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';

import { InsuranceRegistrationRoutingResolveService } from './insurance-registration-routing-resolve.service';

describe('InsuranceRegistration routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InsuranceRegistrationRoutingResolveService;
  let service: InsuranceRegistrationService;
  let resultInsuranceRegistration: IInsuranceRegistration | null | undefined;

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
    routingResolveService = TestBed.inject(InsuranceRegistrationRoutingResolveService);
    service = TestBed.inject(InsuranceRegistrationService);
    resultInsuranceRegistration = undefined;
  });

  describe('resolve', () => {
    it('should return IInsuranceRegistration returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInsuranceRegistration = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInsuranceRegistration).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInsuranceRegistration = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInsuranceRegistration).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IInsuranceRegistration>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInsuranceRegistration = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInsuranceRegistration).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
