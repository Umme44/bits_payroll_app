import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

import { FlexScheduleApplicationUserService } from './flex-schedule-application-user.service';
import { FlexScheduleApplicationUserComponent } from './flex-schedule-application-user.component';
import { FlexScheduleApplicationDetailUserModalComponent } from './flex-schedule-application-detail-user-modal.component';
import { FlexScheduleApplicationUpdateUserComponent } from './flex-schedule-application-update-user.component';
import { FlexScheduleApplication, IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationResolve implements Resolve<IFlexScheduleApplication> {
  constructor(private service: FlexScheduleApplicationUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlexScheduleApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((flexScheduleApplication: HttpResponse<FlexScheduleApplication>) => {
          if (flexScheduleApplication.body) {
            return of(flexScheduleApplication.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FlexScheduleApplication());
  }
}

export const flexScheduleApplicationUserRoute: Routes = [
  {
    path: '',
    component: FlexScheduleApplicationUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlexScheduleApplicationDetailUserModalComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlexScheduleApplicationUpdateUserComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlexScheduleApplicationUpdateUserComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
