import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';
import { EmployeePinConfigurationService } from '../service/employee-pin-configuration.service';

import { EmployeePinConfigurationRoutingResolveService } from './employee-pin-configuration-routing-resolve.service';

describe('EmployeePinConfiguration routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeePinConfigurationRoutingResolveService;
  let service: EmployeePinConfigurationService;
  let resultEmployeePinConfiguration: IEmployeePinConfiguration | null | undefined;

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
    routingResolveService = TestBed.inject(EmployeePinConfigurationRoutingResolveService);
    service = TestBed.inject(EmployeePinConfigurationService);
    resultEmployeePinConfiguration = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeePinConfiguration returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeePinConfiguration = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeePinConfiguration).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeePinConfiguration = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeePinConfiguration).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmployeePinConfiguration>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeePinConfiguration = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeePinConfiguration).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
