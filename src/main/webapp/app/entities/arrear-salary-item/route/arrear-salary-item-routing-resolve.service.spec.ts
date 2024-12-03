import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { ArrearSalaryItemService } from '../service/arrear-salary-item.service';

import { ArrearSalaryItemRoutingResolveService } from './arrear-salary-item-routing-resolve.service';

describe('ArrearSalaryItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ArrearSalaryItemRoutingResolveService;
  let service: ArrearSalaryItemService;
  let resultArrearSalaryItem: IArrearSalaryItem | null | undefined;

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
    routingResolveService = TestBed.inject(ArrearSalaryItemRoutingResolveService);
    service = TestBed.inject(ArrearSalaryItemService);
    resultArrearSalaryItem = undefined;
  });

  describe('resolve', () => {
    it('should return IArrearSalaryItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArrearSalaryItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArrearSalaryItem).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArrearSalaryItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultArrearSalaryItem).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IArrearSalaryItem>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArrearSalaryItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArrearSalaryItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
