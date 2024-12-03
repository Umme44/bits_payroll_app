import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVehicleRequisition } from '../vehicle-requisition.model';
import { VehicleRequisitionService } from '../service/vehicle-requisition.service';

import { VehicleRequisitionRoutingResolveService } from './vehicle-requisition-routing-resolve.service';

describe('VehicleRequisition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VehicleRequisitionRoutingResolveService;
  let service: VehicleRequisitionService;
  let resultVehicleRequisition: IVehicleRequisition | null | undefined;

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
    routingResolveService = TestBed.inject(VehicleRequisitionRoutingResolveService);
    service = TestBed.inject(VehicleRequisitionService);
    resultVehicleRequisition = undefined;
  });

  describe('resolve', () => {
    it('should return IVehicleRequisition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleRequisition).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleRequisition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVehicleRequisition).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IVehicleRequisition>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleRequisition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
