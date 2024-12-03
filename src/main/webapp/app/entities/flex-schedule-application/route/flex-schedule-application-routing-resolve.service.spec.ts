import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFlexScheduleApplication } from '../flex-schedule-application.model';
import { FlexScheduleApplicationService } from '../service/flex-schedule-application.service';

import { FlexScheduleApplicationRoutingResolveService } from './flex-schedule-application-routing-resolve.service';

describe('FlexScheduleApplication routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FlexScheduleApplicationRoutingResolveService;
  let service: FlexScheduleApplicationService;
  let resultFlexScheduleApplication: IFlexScheduleApplication | null | undefined;

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
    routingResolveService = TestBed.inject(FlexScheduleApplicationRoutingResolveService);
    service = TestBed.inject(FlexScheduleApplicationService);
    resultFlexScheduleApplication = undefined;
  });

  describe('resolve', () => {
    it('should return IFlexScheduleApplication returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFlexScheduleApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFlexScheduleApplication).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFlexScheduleApplication = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFlexScheduleApplication).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFlexScheduleApplication>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFlexScheduleApplication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFlexScheduleApplication).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
