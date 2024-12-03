import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAttendanceSyncCache } from '../attendance-sync-cache.model';
import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';

import { AttendanceSyncCacheRoutingResolveService } from './attendance-sync-cache-routing-resolve.service';

describe('AttendanceSyncCache routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AttendanceSyncCacheRoutingResolveService;
  let service: AttendanceSyncCacheService;
  let resultAttendanceSyncCache: IAttendanceSyncCache | null | undefined;

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
    routingResolveService = TestBed.inject(AttendanceSyncCacheRoutingResolveService);
    service = TestBed.inject(AttendanceSyncCacheService);
    resultAttendanceSyncCache = undefined;
  });

  describe('resolve', () => {
    it('should return IAttendanceSyncCache returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendanceSyncCache = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAttendanceSyncCache).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendanceSyncCache = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAttendanceSyncCache).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAttendanceSyncCache>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendanceSyncCache = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAttendanceSyncCache).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
