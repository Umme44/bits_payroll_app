import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';

import { RecruitmentRequisitionBudgetRoutingResolveService } from './recruitment-requisition-budget-routing-resolve.service';

describe('RecruitmentRequisitionBudget routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RecruitmentRequisitionBudgetRoutingResolveService;
  let service: RecruitmentRequisitionBudgetService;
  let resultRecruitmentRequisitionBudget: IRecruitmentRequisitionBudget | null | undefined;

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
    routingResolveService = TestBed.inject(RecruitmentRequisitionBudgetRoutingResolveService);
    service = TestBed.inject(RecruitmentRequisitionBudgetService);
    resultRecruitmentRequisitionBudget = undefined;
  });

  describe('resolve', () => {
    it('should return IRecruitmentRequisitionBudget returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecruitmentRequisitionBudget = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRecruitmentRequisitionBudget).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecruitmentRequisitionBudget = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRecruitmentRequisitionBudget).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IRecruitmentRequisitionBudget>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecruitmentRequisitionBudget = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRecruitmentRequisitionBudget).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
