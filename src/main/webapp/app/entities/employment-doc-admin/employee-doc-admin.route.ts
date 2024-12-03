import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { EmployeeDocsAdminComponent } from './list-view/employee-doc-admin.component';
import { EmployeeNOCDetailAdminComponent } from 'app/entities/employment-doc-admin/employee-noc-admin/details/employee-noc-detail-page-admin.component';
import { EmployeeNocAdminService } from 'app/entities/employment-doc-admin/employee-noc-admin/service/employee-noc-admin.service';
import { EmployeeNOCPrintFormatComponent } from 'app/entities/employment-doc-admin/employee-noc-admin/print-format/employee-noc-print-format.component';
import { EmploymentCertificateDetailAdminComponent } from 'app/entities/employment-doc-admin/employment-certificate-admin/details/employment-certificate-detail-admin.component';
import { EmploymentCertificateAdminService } from 'app/entities/employment-doc-admin/employment-certificate-admin/service/employment-certificate-admin.service';
import { EmploymentCertificatePrintFormatComponent } from 'app/entities/employment-doc-admin/employment-certificate-admin/print-format/employment-certificate-print-format.component';
import { EmployeeSalaryCertificatePrintFormatComponent } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/print-format/employee-salary-certificate-print-format.component';
import { EmployeeSalaryCertificateDetailAdminComponent } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/details/employee-salary-certificate-detail-page-admin.component';
import { EmployeeSalaryCertificateAdminService } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/service/employee-salary-certificate-admin.service';
import { IEmployeeNOC } from './model/employee-noc.model';
import { IEmploymentCertificate } from './model/employment-certificate.model';
import { ISalaryCertificate } from './model/salary-certificate.model';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

// Employee NOC Admin Resolver
@Injectable({ providedIn: 'root' })
export class EmployeeNOCResolve implements Resolve<IEmployeeNOC> {
  constructor(private service: EmployeeNocAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeNOC> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findEmployeeNocById(id).pipe(
        flatMap((employeeNOC: HttpResponse<IEmployeeNOC>) => {
          if (employeeNOC.body) {
            return of(employeeNOC.body);
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

// Employee NOC Print Format Admin Resolver
@Injectable({ providedIn: 'root' })
export class EmployeeNOCPrintFormatResolve implements Resolve<IEmployeeNOC> {
  constructor(private service: EmployeeNocAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeNOC> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findEmployeeNocPrintFormatById(id).pipe(
        flatMap((employeeNOC: HttpResponse<IEmployeeNOC>) => {
          if (employeeNOC.body) {
            return of(employeeNOC.body);
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

// Employment Certificate Admin Resolver
@Injectable({ providedIn: 'root' })
export class EmploymentCertificateAdminResolve implements Resolve<IEmploymentCertificate> {
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

// Employment Certificate Print Format Resolver
@Injectable({ providedIn: 'root' })
export class EmploymentCertificatePrintFormatResolve implements Resolve<IEmploymentCertificate> {
  constructor(private service: EmploymentCertificateAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findPrintFormatById(id).pipe(
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

// Employee Salary Certificate Admin Resolver
@Injectable({ providedIn: 'root' })
export class EmployeeSalaryCertificateResolve implements Resolve<ISalaryCertificate> {
  constructor(private service: EmployeeSalaryCertificateAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salaryCertificate: HttpResponse<ISalaryCertificate>) => {
          if (salaryCertificate.body) {
            return of(salaryCertificate.body);
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

// Employee Salary Certificate Print Format Admin Resolver
@Injectable({ providedIn: 'root' })
export class EmployeeSalaryCertificatePrintFormatResolve implements Resolve<ISalaryCertificate> {
  constructor(private service: EmployeeSalaryCertificateAdminService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryCertificate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salaryCertificate: HttpResponse<ISalaryCertificate>) => {
          if (salaryCertificate.body) {
            return of(salaryCertificate.body);
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

export const employeeDOCAdminRoute: Routes = [
  {
    path: '',
    component: EmployeeDocsAdminComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employee-noc/:id/print-format',
    component: EmployeeNOCPrintFormatComponent,
    resolve: {
      employeeNOC: EmployeeNOCPrintFormatResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employee-noc/:id/view',
    component: EmployeeNOCDetailAdminComponent,
    resolve: {
      employeeNOC: EmployeeNOCResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'employment-certificate/:id/print-format',
    component: EmploymentCertificatePrintFormatComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateAdminResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employment-certificate/:id/view',
    component: EmploymentCertificateDetailAdminComponent,
    resolve: {
      employmentCertificate: EmploymentCertificateAdminResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'employee-salary-certificate/:id/print-format',
    component: EmployeeSalaryCertificatePrintFormatComponent,
    resolve: {
      salaryCertificate: EmployeeSalaryCertificatePrintFormatResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeDocs.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employee-salary-certificate/:id/view',
    component: EmployeeSalaryCertificateDetailAdminComponent,
    resolve: {
      salaryCertificate: EmployeeSalaryCertificateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeDocs.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
