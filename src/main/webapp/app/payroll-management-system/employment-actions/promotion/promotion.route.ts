import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PromotionListComponent } from './promotion-list.component';
import { PromotionDetailComponent } from './promotion-detail.component';
import { PromotionCreateComponent } from './promotion-create.component';
import { PromotionUpdateComponent } from './promotion-update.component';
import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { EmploymentHistoryService } from '../../../shared/legacy/legacy-service/employment-history.service';
import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryResolve implements Resolve<IEmploymentHistory> {
  constructor(private service: EmploymentHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employmentHistory: HttpResponse<EmploymentHistory>) => {
          if (employmentHistory.body) {
            return of(employmentHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmploymentHistory());
  }
}

export const PROMOTION_ROUTE: Routes = [
  {
    path: 'promotion',
    component: PromotionListComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.promotion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promotion/:id/view',
    component: PromotionDetailComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.promotion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promotion/new',
    component: PromotionCreateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.promotion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promotion/:id/edit',
    component: PromotionUpdateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.promotion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
