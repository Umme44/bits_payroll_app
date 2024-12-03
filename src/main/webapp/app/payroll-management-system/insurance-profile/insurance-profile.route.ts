import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { InsuranceProfileComponent } from './insurance-profile.component';
import { IInsuranceClaim, InsuranceClaim } from '../../shared/legacy/legacy-model/insurance-claim.model';
import { UserInsuranceService } from './user-insurance.service';
import { IInsuranceRegistration, InsuranceRegistration } from '../../shared/legacy/legacy-model/insurance-registration.model';
import { UserInsuranceRegistrationUpdateComponent } from './user-insurance-registration-update.component';
import { UserInsuranceRegistrationDetailsComponent } from './user-insurance-registration-details.component';
import { UserInsuranceClaimDetailComponent } from './user-insurance-claim-detail.component';

@Injectable({ providedIn: 'root' })
export class UserInsuranceClaimResolve implements Resolve<IInsuranceClaim> {
  constructor(private service: UserInsuranceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInsuranceClaim> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findInsuranceRegistration(id).pipe(
        flatMap((insuranceClaim: HttpResponse<InsuranceClaim>) => {
          if (insuranceClaim.body) {
            return of(insuranceClaim.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InsuranceClaim());
  }
}

// Insurance Registration Page Resolver
@Injectable({ providedIn: 'root' })
export class InsuranceRegistrationResolve implements Resolve<IInsuranceRegistration> {
  constructor(private service: UserInsuranceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInsuranceRegistration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findInsuranceRegistration(id).pipe(
        flatMap((insuranceRegistration: HttpResponse<InsuranceRegistration>) => {
          if (insuranceRegistration.body) {
            return of(insuranceRegistration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InsuranceRegistration());
  }
}

export const insuranceProfileRoute: Routes = [
  {
    path: '',
    component: InsuranceProfileComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  // Routes For Registration
  {
    path: 'registration',
    component: UserInsuranceRegistrationUpdateComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'registration/:id/edit',
    component: UserInsuranceRegistrationUpdateComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'registration/:id/view',
    component: UserInsuranceRegistrationDetailsComponent,
    resolve: {
      insuranceRegistration: InsuranceRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'claim-entry/:id/view',
    component: UserInsuranceClaimDetailComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.insuranceRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
