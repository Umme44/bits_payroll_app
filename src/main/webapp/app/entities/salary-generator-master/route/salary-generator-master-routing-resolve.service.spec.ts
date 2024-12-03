import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISalaryGeneratorMaster } from '../salary-generator-master.model';
import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';

import { SalaryGeneratorMasterRoutingResolveService } from './salary-generator-master-routing-resolve.service';

describe('SalaryGeneratorMaster routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SalaryGeneratorMasterRoutingResolveService;
  let service: SalaryGeneratorMasterService;
  let resultSalaryGeneratorMaster: ISalaryGeneratorMaster | null | undefined;

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
    routingResolveService = TestBed.inject(SalaryGeneratorMasterRoutingResolveService);
    service = TestBed.inject(SalaryGeneratorMasterService);
    resultSalaryGeneratorMaster = undefined;
  });

  describe('resolve', () => {
    it('should return ISalaryGeneratorMaster returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSalaryGeneratorMaster = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSalaryGeneratorMaster).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSalaryGeneratorMaster = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSalaryGeneratorMaster).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISalaryGeneratorMaster>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSalaryGeneratorMaster = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSalaryGeneratorMaster).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
