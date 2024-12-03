import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { NomineeService } from '../service/nominee.service';
import { INominee } from '../nominee.model';
import { NomineeComponent } from '../list/nominee.component';
import { Authority } from '../../../config/authority.constants';
import { NomineeSummaryAdminComponent } from '../summary-view/nominee-summary-admin.component';
import { AdminGeneralNomineeUpdatePolishedComponent } from '../update/general-nominee/admin-general-nominee-update-polished.component';
import { NomineeType } from '../../../shared/model/enumerations/nominee-type.model';
import { AdminGfNomineeUpdatePolishedComponent } from '../update/gf-nominee/admin--gf-nominee-update-polished.component';
import { IPfNominee, PfNominee } from '../pf-nominee.model';
import { PfNomineeService } from '../service/pf-nominee.service';
import { PfNomineeUpdateComponent } from '../update/pf-nominee/pf-nominee-update.component';
import { GeneralNominationReportComponent } from '../reports/general-nominee/general-nomination-report.component';
import { GfNomineeReportComponent } from '../reports/gf-nominee/gf-nominee-report.component';
import { PfNomineeReportAdminComponent } from '../reports/pf-nominee/pf-nominee-report-admin.component';

@Injectable({ providedIn: 'root' })
export class NomineeResolve implements Resolve<INominee> {
  constructor(private service: NomineeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INominee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((nominee: HttpResponse<INominee>) => {
          if (nominee.body) {
            return of(nominee.body);
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

@Injectable({ providedIn: 'root' })
export class NomineeResolveForUser implements Resolve<INominee> {
  constructor(private service: NomineeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INominee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findNomineeCommon(id).pipe(
        flatMap((nominee: HttpResponse<INominee>) => {
          if (nominee.body) {
            return of(nominee.body);
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

@Injectable({ providedIn: 'root' })
export class PfNomineeResolve implements Resolve<IPfNominee> {
  constructor(private service: PfNomineeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfNominee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pfNominee: HttpResponse<PfNominee>) => {
          if (pfNominee.body) {
            return of(pfNominee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PfNominee());
  }
}

export const ACCESS_GROUP_EMPLOYEE = 'employee';

export const nomineeRoute: Routes = [
  {
    path: '',
    component: NomineeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'summary/:pin/view',
    component: NomineeSummaryAdminComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'general/edit/:id',
    component: AdminGeneralNomineeUpdatePolishedComponent,
    resolve: {
      nominee: NomineeResolve,
    },
    data: {
      nomineeType: NomineeType.GENERAL,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'gf/edit/:id',
    component: AdminGfNomineeUpdatePolishedComponent,
    resolve: {
      nominee: NomineeResolve,
    },
    data: {
      nomineeType: NomineeType.GRATUITY_FUND,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'pf-nominee/:id/edit',
    component: PfNomineeUpdateComponent,
    resolve: {
      pfNominee: PfNomineeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'general-form/nominee-report/:id',
    component: GeneralNominationReportComponent,
    data: {
      nomineeType: NomineeType.GENERAL,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'gf-form/nominee-report/:id',
    component: GfNomineeReportComponent,
    data: {
      nomineeType: NomineeType.GENERAL,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'pf-nominee/print/:pin',
    component: PfNomineeReportAdminComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  // {
  //   path: 'gf-form/nominee-report',
  //   component: GfNomineeReportComponent,
  //   resolve: {
  //     nominee: NomineeResolveForUser,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general-form/nominee-report',
  //   component: GeneralNominationReportComponent,
  //   resolve: {
  //     nominee: NomineeResolveForUser,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GENERAL,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general-form/:id/view',
  //   component: NomineeDetailPolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolveForUser,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'gf-form/:id/view',
  //   component: NomineeDetailPolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolveForUser,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // // {
  // //   path: 'gf-form',
  // //   component: NomineeUpdatePolishedComponent,
  // //   resolve: {
  // //     nominee: NomineeResolveForUser,
  // //   },
  // //   data: {
  // //     nomineeType: NomineeType.GRATUITY_FUND,
  // //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  // //     authorities: [Authority.USER],
  // //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  // //   },
  // //   canActivate: [UserRouteAccessService],
  // // },
  // //
  // // {
  // //   path: 'general-form',
  // //   component: NomineeUpdatePolishedComponent,
  // //   resolve: {
  // //     nominee: NomineeResolveForUser,
  // //   },
  // //   data: {
  // //     nomineeType: NomineeType.GENERAL,
  // //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  // //     authorities: [Authority.USER],
  // //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  // //   },
  // //   canActivate: [UserRouteAccessService],
  // // },
  //
  // {
  //   path: 'summary/:pin/view',
  //   component: NomineeSummaryAdminComponent,
  //   data: {
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.title',
  //     authorities: [Authority.USER],
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'gf-nominee/approval',
  //   component: GfNomineeApprovalComponent,
  //   data: {
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITYApproval',
  //     authorities: [Authority.USER],
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general-nominee/approval',
  //   component: GeneralNomineeApprovalComponent,
  //   data: {
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneralApproval',
  //     authorities: [Authority.USER],
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general-form/nominee-report/:id',
  //   component: GeneralNomineeAdminReportComponent,
  //   data: {
  //     nomineeType: NomineeType.GENERAL,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'gf-form/nominee-report/:id',
  //   component: GfNomineeAdminReportComponent,
  //   data: {
  //     nomineeType: NomineeType.GENERAL,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general/new',
  //   component: AdminGeneralNomineeUpdatePolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GENERAL,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general/edit/:id',
  //   component: AdminGeneralNomineeUpdatePolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GENERAL,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'gf/new',
  //   component: AdminGfNomineeUpdatePolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'gf/edit/:id',
  //   component: AdminGfNomineeUpdatePolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'gf/:id/view',
  //   component: AdminGfNomineeDetailPolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  //
  // {
  //   path: 'general/:id/view',
  //   component: AdminGeneralNomineeDetailPolishedComponent,
  //   resolve: {
  //     nominee: NomineeResolve,
  //   },
  //   data: {
  //     nomineeType: NomineeType.GRATUITY_FUND,
  //     accessGroup: ACCESS_GROUP_EMPLOYEE,
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.nominee.home.titleGRATUITY',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
];
