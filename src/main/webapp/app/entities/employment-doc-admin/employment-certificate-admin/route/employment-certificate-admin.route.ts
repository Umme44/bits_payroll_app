import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../../config/authority.constants';
import { UserRouteAccessService } from '../../../../core/auth/user-route-access.service';
import { EmploymentCertificateAdminService } from '../service/employment-certificate-admin.service';
import { EmploymentCertificateDetailAdminComponent } from '../details/employment-certificate-detail-admin.component';
import { IEmploymentCertificate } from '../../model/employment-certificate.model';

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateResolve implements Resolve<IEmploymentCertificate> {
  constructor(private service: EmploymentCertificateAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employmentCertificate: HttpResponse<IEmploymentCertificate>) => {
          if (employmentCertificate.body) {
            return of(employmentCertificate.body);
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

export const employmentCertificateAdminRoute: Routes = [
  {
    path: ':id/view',
    component: EmploymentCertificateDetailAdminComponent,
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
