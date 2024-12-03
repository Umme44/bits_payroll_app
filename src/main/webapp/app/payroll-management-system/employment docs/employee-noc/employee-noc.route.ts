import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { EmployeeNOCService } from './employee-noc.service';
import { EmployeeNOCDetailComponent } from './employee-noc-detail.component';
import { EmployeeNOCUpdateComponent } from './employee-noc-update.component';
import { EmployeeNOC, IEmployeeNOC } from '../../../shared/legacy/legacy-model/employee-noc.model';

@Injectable({ providedIn: 'root' })
export class EmployeeNOCResolve implements Resolve<IEmployeeNOC> {
  constructor(private service: EmployeeNOCService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeNOC> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employeeNOC: HttpResponse<EmployeeNOC>) => {
          if (employeeNOC.body) {
            return of(employeeNOC.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeNOC());
  }
}

export const employeeNOCRoute: Routes = [
  {
    path: ':id/view',
    component: EmployeeNOCDetailComponent,
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
    path: 'new',
    component: EmployeeNOCUpdateComponent,
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
    path: ':id/edit',
    component: EmployeeNOCUpdateComponent,
    resolve: {
      employeeNOC: EmployeeNOCResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
