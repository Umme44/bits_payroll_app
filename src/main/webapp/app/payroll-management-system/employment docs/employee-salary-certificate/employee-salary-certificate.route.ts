import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { EmployeeSalaryCertificateService } from './employee-salary-certificate.service';
import { EmployeeSalaryCertificateDetailComponent } from './employee-salary-certificate-detail.component';
import { EmployeeSalaryCertificateUpdateComponent } from './employee-salary-certificate-update.component';
import { ISalaryCertificate, SalaryCertificate } from '../../../shared/legacy/legacy-model/salary-certificate.model';
import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryCertificateResolve implements Resolve<ISalaryCertificate> {
  constructor(private service: EmployeeSalaryCertificateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salaryCertificate: HttpResponse<SalaryCertificate>) => {
          if (salaryCertificate.body) {
            return of(salaryCertificate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SalaryCertificate());
  }
}

export const employeeSalaryCertificateRoute: Routes = [
  {
    path: ':id/view',
    component: EmployeeSalaryCertificateDetailComponent,
    resolve: {
      salaryCertificate: EmployeeSalaryCertificateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeSalaryCertificateUpdateComponent,
    resolve: {
      salaryCertificate: EmployeeSalaryCertificateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeSalaryCertificateUpdateComponent,
    resolve: {
      salaryCertificate: EmployeeSalaryCertificateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.salaryCertificate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
