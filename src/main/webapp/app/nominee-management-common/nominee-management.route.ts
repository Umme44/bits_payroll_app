import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { NomineeMasterCommonComponent } from 'app/nominee-management-common/nominee-master-common/nominee-master-common.component';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { Authority } from '../config/authority.constants';
import { NomineeType } from '../shared/model/enumerations/nominee-type.model';
import { PfNomineeFormPrintDetailsComponent } from './report/pf-nominee-report/pf-nominee-form-details-print-format';
import { NomineeDetailPolishedComponent } from './details/general-and-gf-nominee/nominee-detail-polished.component';
import { INominee, Nominee } from '../entities/nominee-admin/nominee.model';
import { Injectable } from '@angular/core';
import { NomineeService } from '../entities/nominee-admin/service/nominee.service';
import { EMPTY, Observable, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { flatMap } from 'rxjs/operators';
import { GfNomineeReportComponent } from './report/gf-nominee/gf-nominee-report.component';
import { GeneralNominationReportComponent } from './report/general-nominee/general-nomination-report.component';
import { PfNomineeFormDetailComponent } from './details/pf-nominee/pf-nominee-form-detail.component';
import { IPfNominee, PfNominee } from '../shared/legacy/legacy-model/pf-nominee.model';
import { PfNomineeFormService } from '../provident-fund-management/pf-nominee-form/pf-nominee-form.service';

export const ACCESS_GROUP_EMPLOYEE = 'employee';

@Injectable({ providedIn: 'root' })
export class NomineeResolveForUser implements Resolve<INominee> {
  constructor(private service: NomineeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INominee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findNomineeCommon(id).pipe(
        flatMap((nominee: HttpResponse<Nominee>) => {
          if (nominee.body) {
            return of(nominee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Nominee());
  }
}

@Injectable({ providedIn: 'root' })
export class PfNomineeFormResolve implements Resolve<IPfNominee> {
  constructor(private service: PfNomineeFormService, private router: Router) {}

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

export const nomineeManagementCommonRoutes: Routes = [
  {
    path: 'dashboard',
    component: NomineeMasterCommonComponent,
    data: {
      authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'gf/report',
    component: GfNomineeReportComponent,
    data: {
      nomineeType: NomineeType.GRATUITY_FUND,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'general/report',
    component: GeneralNominationReportComponent,
    data: {
      nomineeType: NomineeType.GENERAL,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'pf/report',
    component: PfNomineeFormPrintDetailsComponent,
    data: {
      authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'general/:id/view',
    component: NomineeDetailPolishedComponent,
    resolve: {
      nominee: NomineeResolveForUser,
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
    path: 'gf/:id/view',
    component: NomineeDetailPolishedComponent,
    resolve: {
      nominee: NomineeResolveForUser,
    },
    data: {
      nomineeType: NomineeType.GRATUITY_FUND,
      accessGroup: ACCESS_GROUP_EMPLOYEE,
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.nominee.home.titleGeneral',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'nominee/pf/:id/view',
    component: PfNomineeFormDetailComponent,
    resolve: {
      pfNominee: PfNomineeFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
