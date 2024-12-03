import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserWorkFromHomeApplicationService } from '../user-work-from-home-application/service/user-work-from-home-application.service';
import {
  IUserWorkFromHomeApplication,
  UserWorkFromHomeApplication,
} from '../user-work-from-home-application/user-work-from-home-application.model';
import { WorkFromHomeApplicationsApprovalsLMComponent } from './work-from-home-applications-approvals-lm.component';
import { WorkFromHomeApplicationsApprovalsHRComponent } from './work-from-home-applications-approvals-hr.component';

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationResolve implements Resolve<IUserWorkFromHomeApplication> {
  constructor(private service: UserWorkFromHomeApplicationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserWorkFromHomeApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((workFromHomeApplication: HttpResponse<UserWorkFromHomeApplication>) => {
          if (workFromHomeApplication.body) {
            return of(workFromHomeApplication.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

export const workFromHomeApplicationApprovalsRoute: Routes = [
  {
    path: '',
    component: WorkFromHomeApplicationsApprovalsHRComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplicationApprovals.home',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'lm',
    component: WorkFromHomeApplicationsApprovalsLMComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.workFromHomeApplicationApprovals.home',
    },
    canActivate: [UserRouteAccessService],
  },
];
