import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeeDocument } from '../employee-document.model';
import { EmployeeDocumentService } from '../service/employee-document.service';

import { EmployeeDocumentRoutingResolveService } from './employee-document-routing-resolve.service';

describe('EmployeeDocument routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeeDocumentRoutingResolveService;
  let service: EmployeeDocumentService;
  let resultEmployeeDocument: IEmployeeDocument | null | undefined;

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
    routingResolveService = TestBed.inject(EmployeeDocumentRoutingResolveService);
    service = TestBed.inject(EmployeeDocumentService);
    resultEmployeeDocument = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeeDocument returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeDocument = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeDocument).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeDocument = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeeDocument).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmployeeDocument>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeDocument = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeDocument).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
