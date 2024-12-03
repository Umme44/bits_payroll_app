import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import { ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';

import { ProRataFestivalBonusRoutingResolveService } from './pro-rata-festival-bonus-routing-resolve.service';

describe('ProRataFestivalBonus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProRataFestivalBonusRoutingResolveService;
  let service: ProRataFestivalBonusService;
  let resultProRataFestivalBonus: IProRataFestivalBonus | null | undefined;

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
    routingResolveService = TestBed.inject(ProRataFestivalBonusRoutingResolveService);
    service = TestBed.inject(ProRataFestivalBonusService);
    resultProRataFestivalBonus = undefined;
  });

  describe('resolve', () => {
    it('should return IProRataFestivalBonus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProRataFestivalBonus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProRataFestivalBonus).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProRataFestivalBonus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProRataFestivalBonus).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IProRataFestivalBonus>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProRataFestivalBonus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProRataFestivalBonus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
