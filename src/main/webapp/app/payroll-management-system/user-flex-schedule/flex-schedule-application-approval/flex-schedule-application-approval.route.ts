import { Injectable } from '@angular/core';

import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { FlexScheduleApplicationApprovalComponent } from './flex-schedule-application-approval.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlexScheduleApplicationApprovalService } from './flex-schedule-application-approval.service';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { FlexScheduleApplicationApprovedByMeComponent } from './flex-schedule-application-approved-by-me.component';

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationApprovalResolve implements Resolve<IFlexScheduleApplication[]> {
  constructor(private service: FlexScheduleApplicationApprovalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlexScheduleApplication[]> {
    const pageType = route.params['pageType'];
    if (pageType) {
      if (pageType === 'hr') {
        return this.service.findAllPendingHR().pipe(
          flatMap((flexScheduleApplication: HttpResponse<IFlexScheduleApplication[]>) => {
            if (flexScheduleApplication.body) {
              return of(flexScheduleApplication.body);
            } else {
              return EMPTY;
            }
          })
        );
      } else if (pageType === 'lm') {
        return this.service.findAllPendingLM().pipe(
          flatMap((flexScheduleApplication: HttpResponse<IFlexScheduleApplication[]>) => {
            if (flexScheduleApplication.body) {
              return of(flexScheduleApplication.body);
            } else {
              return EMPTY;
            }
          })
        );
      }
    }
    return new Observable<IFlexScheduleApplication[]>();
  }
}

export const FLEX_SCHEDULE_APPLICATION_APPROVAL_ROUTE: Routes = [
  {
    path: ':pageType',
    component: FlexScheduleApplicationApprovalComponent,
    resolve: {
      flexScheduleApplication: FlexScheduleApplicationApprovalResolve,
    },
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApproval.home',
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':pageType',
  //   component: FlexScheduleApplicationApprovalComponent,
  //   resolve: {
  //     flexScheduleApplication: FlexScheduleApplicationApprovalResolve,
  //   },
  //   data: {
  //     authorities: [],
  //     pageTitle: 'bitsHrPayrollApp.flexScheduleApproval.home',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  {
    path: ':pageType/approved-by-me',
    component: FlexScheduleApplicationApprovedByMeComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.flexScheduleApproval.home',
    },
    canActivate: [UserRouteAccessService],
  },
];
