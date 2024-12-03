import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeeNOC } from '../employee-noc.model';
import { EmployeeNOCService } from '../service/employee-noc.service';

import { EmployeeNOCRoutingResolveService } from './employee-noc-routing-resolve.service';

describe('EmployeeNOC routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeeNOCRoutingResolveService;
  let service: EmployeeNOCService;
  let resultEmployeeNOC: IEmployeeNOC | null | undefined;

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
    routingResolveService = TestBed.inject(EmployeeNOCRoutingResolveService);
    service = TestBed.inject(EmployeeNOCService);
    resultEmployeeNOC = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeeNOC returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeNOC = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeNOC).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeNOC = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeeNOC).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmployeeNOC>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeNOC = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeNOC).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
