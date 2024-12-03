import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { EmploymentCertificateService } from './employment-certificate.service';
import { EmploymentCertificateDetailComponent } from './employment-certificate-detail.component';
import { EmploymentCertificate, IEmploymentCertificate } from '../../../shared/legacy/legacy-model/employment-certificate.model';

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateResolve implements Resolve<IEmploymentCertificate> {
  constructor(private service: EmploymentCertificateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employmentCertificate: HttpResponse<EmploymentCertificate>) => {
          if (employmentCertificate.body) {
            return of(employmentCertificate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmploymentCertificate());
  }
}

export const employmentCertificateRoute: Routes = [
  {
    path: ':id/view',
    component: EmploymentCertificateDetailComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employmentCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
