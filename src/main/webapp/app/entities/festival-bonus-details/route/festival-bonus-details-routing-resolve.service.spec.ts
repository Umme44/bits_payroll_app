import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import { FestivalBonusDetailsService } from '../service/festival-bonus-details.service';

import { FestivalBonusDetailsRoutingResolveService } from './festival-bonus-details-routing-resolve.service';

describe('FestivalBonusDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FestivalBonusDetailsRoutingResolveService;
  let service: FestivalBonusDetailsService;
  let resultFestivalBonusDetails: IFestivalBonusDetails | null | undefined;

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
    routingResolveService = TestBed.inject(FestivalBonusDetailsRoutingResolveService);
    service = TestBed.inject(FestivalBonusDetailsService);
    resultFestivalBonusDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IFestivalBonusDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFestivalBonusDetails).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFestivalBonusDetails).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFestivalBonusDetails>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFestivalBonusDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFestivalBonusDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
