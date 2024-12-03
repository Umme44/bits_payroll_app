import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFestivalBonusConfig } from '../festival-bonus-config.model';
import { FestivalBonusConfigService } from '../service/festival-bonus-config.service';

import { FestivalBonusConfigRoutingResolveService } from './festival-bonus-config-routing-resolve.service';

describe('FestivalBonusConfig routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FestivalBonusConfigRoutingResolveService;
  let service: FestivalBonusConfigService;
  let resultFestivalBonusConfig: IFestivalBonusConfig | null | undefined;

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
    routingResolveService = TestBed.inject(FestivalBonusConfigRoutingResolveService);
    service = TestBed.inject(FestivalBonusConfigService);
    resultFestivalBonusConfig = undefined;
  });

  describe('resolve', () => {
    it('should return IFestivalBonusConfig returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusConfig = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFestivalBonusConfig).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusConfig = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFestivalBonusConfig).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFestivalBonusConfig>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusConfig = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFestivalBonusConfig).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
