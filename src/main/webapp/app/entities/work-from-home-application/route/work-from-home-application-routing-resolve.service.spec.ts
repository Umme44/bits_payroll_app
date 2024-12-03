import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWorkFromHomeApplication } from '../work-from-home-application.model';
import { WorkFromHomeApplicationService } from '../service/work-from-home-application.service';

import { WorkFromHomeApplicationRoutingResolveService } from './work-from-home-application-routing-resolve.service';

describe('WorkFromHomeApplication routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WorkFromHomeApplicationRoutingResolveService;
  let service: WorkFromHomeApplicationService;
  let resultWorkFromHomeApplication: IWorkFromHomeApplication | null | undefined;

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
    routingResolveService = TestBed.inject(WorkFromHomeApplicationRoutingResolveService);
    service = TestBed.inject(WorkFromHomeApplicationService);
    resultWorkFromHomeApplication = undefined;
  });

  describe('resolve', () => {
    it('should return IWorkFromHomeApplication returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkFromHomeApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkFromHomeApplication).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkFromHomeApplication = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWorkFromHomeApplication).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWorkFromHomeApplication>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkFromHomeApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkFromHomeApplication).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
