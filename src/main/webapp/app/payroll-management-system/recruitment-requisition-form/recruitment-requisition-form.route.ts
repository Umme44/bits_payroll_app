import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { Authority } from '../../config/authority.constants';

import { RecruitmentRequisitionFormService } from './recruitment-requisition-form.service';
import { RecruitmentRequisitionFormRaiseOnBehalfComponent } from './raise-on-behalf/recruitment-requisition-form-raise-on-behalf.component';
import { RecruitmentRequisitionFormDetailComponent } from './recruitment-requisition-form-detail.component';
import { UserRecruitmentRequisitionFormUpdateComponent } from './user/user-recruitment-requisition-form-update.component';
import { UserRecruitmentRequisitionFormComponent } from './user/user-recruitment-requisition-form.component';
import { RecruitmentRequisitionFormApprovalComponent } from './approval/recruitment-requisition-form-approval.component';
import { RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent } from './raise-on-behalf/recruitment-requisition-form-raise-on-behalf-update.component';
import {
  IRecruitmentRequisitionForm,
  RecruitmentRequisitionForm,
} from '../../shared/legacy/legacy-model/recruitment-requisition-form.model';

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionFormResolve implements Resolve<IRecruitmentRequisitionForm> {
  constructor(private service: RecruitmentRequisitionFormService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecruitmentRequisitionForm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((recruitmentRequisitionForm: HttpResponse<RecruitmentRequisitionForm>) => {
          if (recruitmentRequisitionForm.body) {
            return of(recruitmentRequisitionForm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecruitmentRequisitionForm());
  }
}

@Injectable({ providedIn: 'root' })
export class UserRecruitmentRequisitionFormResolve implements Resolve<IRecruitmentRequisitionForm> {
  constructor(private service: RecruitmentRequisitionFormService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecruitmentRequisitionForm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findCommon(id).pipe(
        flatMap((recruitmentRequisitionForm: HttpResponse<RecruitmentRequisitionForm>) => {
          if (recruitmentRequisitionForm.body) {
            return of(recruitmentRequisitionForm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecruitmentRequisitionForm());
  }
}

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionFormOnBehalfResolve implements Resolve<IRecruitmentRequisitionForm> {
  constructor(private service: RecruitmentRequisitionFormService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecruitmentRequisitionForm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findOnBehalfCommon(id).pipe(
        flatMap((recruitmentRequisitionForm: HttpResponse<RecruitmentRequisitionForm>) => {
          if (recruitmentRequisitionForm.body) {
            return of(recruitmentRequisitionForm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecruitmentRequisitionForm());
  }
}

export const recruitmentRequisitionFormRoute: Routes = [
  // {
  //   path: '',
  //   component: RecruitmentRequisitionFormComponent,
  //   data: {
  //     authorities: [Authority.USER],
  //     defaultSort: 'id,asc',
  //     pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/view',
  //   component: RecruitmentRequisitionFormDetailComponent,
  //   resolve: {
  //     recruitmentRequisitionForm: RecruitmentRequisitionFormResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: RecruitmentRequisitionFormUpdateComponent,
  //   resolve: {
  //     recruitmentRequisitionForm: RecruitmentRequisitionFormResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   component: RecruitmentRequisitionFormUpdateComponent,
  //   resolve: {
  //     recruitmentRequisitionForm: RecruitmentRequisitionFormResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },

  // raise-on-behalf routes
  {
    path: 'raise-on-behalf',
    component: RecruitmentRequisitionFormRaiseOnBehalfComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'raise-on-behalf/new',
    component: RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormOnBehalfResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'raise-on-behalf/:id/edit',
    component: RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormOnBehalfResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'raise-on-behalf/:id/view',
    component: RecruitmentRequisitionFormDetailComponent,
    resolve: {
      recruitmentRequisitionForm: RecruitmentRequisitionFormOnBehalfResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  // user
  {
    path: 'user/:id/edit',
    component: UserRecruitmentRequisitionFormUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: UserRecruitmentRequisitionFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user/new',
    component: UserRecruitmentRequisitionFormUpdateComponent,
    resolve: {
      recruitmentRequisitionForm: UserRecruitmentRequisitionFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user/:id/view',
    component: RecruitmentRequisitionFormDetailComponent,
    resolve: {
      recruitmentRequisitionForm: UserRecruitmentRequisitionFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user',
    component: UserRecruitmentRequisitionFormComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user/approval',
    component: RecruitmentRequisitionFormApprovalComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionFormApproval.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user/approval/:id/view',
    component: RecruitmentRequisitionFormDetailComponent,
    resolve: {
      recruitmentRequisitionForm: UserRecruitmentRequisitionFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.recruitmentRequisitionForm.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
