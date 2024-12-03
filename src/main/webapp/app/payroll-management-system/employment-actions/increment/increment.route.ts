import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { IncrementListComponent } from './increment-list.component';
import { IncrementDetailComponent } from './increment-detail.component';
import { IncrementCreateComponent } from './increment-create.component';
import { IncrementUpdateComponent } from './increment-update.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { Authority } from '../../../config/authority.constants';
import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { EmploymentHistoryService } from '../../../shared/legacy/legacy-service/employment-history.service';

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryResolve implements Resolve<IEmploymentHistory> {
  constructor(private service: EmploymentHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employmentHistory: HttpResponse<IEmploymentHistory>) => {
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

export const INCREMENT_ROUTE: Routes = [
  {
    path: 'increment',
    component: IncrementListComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.increment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'increment/:id/view',
    component: IncrementDetailComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.increment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'increment/new',
    component: IncrementCreateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.increment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'increment/:id/edit',
    component: IncrementUpdateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.increment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
