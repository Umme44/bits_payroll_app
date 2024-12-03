import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmploymentHistory } from '../employment-history.model';
import { EmploymentHistoryService } from '../service/employment-history.service';

import { EmploymentHistoryRoutingResolveService } from './employment-history-routing-resolve.service';

describe('EmploymentHistory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmploymentHistoryRoutingResolveService;
  let service: EmploymentHistoryService;
  let resultEmploymentHistory: IEmploymentHistory | null | undefined;

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
    routingResolveService = TestBed.inject(EmploymentHistoryRoutingResolveService);
    service = TestBed.inject(EmploymentHistoryService);
    resultEmploymentHistory = undefined;
  });

  describe('resolve', () => {
    it('should return IEmploymentHistory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmploymentHistory).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentHistory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmploymentHistory).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmploymentHistory>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmploymentHistory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
