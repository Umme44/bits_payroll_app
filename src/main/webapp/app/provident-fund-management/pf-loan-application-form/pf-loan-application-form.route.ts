import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { flatMap } from 'rxjs/operators';
import { PfLoanApplicationFormUpdateComponent } from './pf-loan-application-form-update.component';
import { PfLoanApplicationFormComponent } from './pf-loan-application-form.component';
import { PfLoanApplicationFormDetailComponent } from './pf-loan-application-form-details.component';
import { PfLoanApplicationApprovedFormComponent } from './pf-loan-application-approved-form.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { Authority } from '../../config/authority.constants';
import { IPfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';
import { PfLoanApplicationService } from '../../shared/legacy/legacy-service/pf-loan-application.service';

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationFormResolve implements Resolve<IPfLoanApplication> {
  constructor(private service: PfLoanApplicationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfLoanApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pfLoanApplication: HttpResponse<IPfLoanApplication>) => {
          if (pfLoanApplication.body) {
            return of(pfLoanApplication.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    // @ts-ignore
    return of(new NewPfLoanApplication());
  }
}

export const pfLoanApplicationFormRoute: Routes = [
  {
    path: '',
    component: PfLoanApplicationFormComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfLoanApplicationFormDetailComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/approved',
    component: PfLoanApplicationApprovedFormComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfLoanApplicationFormUpdateComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfLoanApplicationFormUpdateComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
