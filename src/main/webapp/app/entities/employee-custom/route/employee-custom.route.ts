import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { EmployeeCustomComponent } from '../list/employee-custom.component';
import { EmployeeCustomDetailComponent } from '../detail/employee-custom-detail.component';
import { EmployeeCustomUpdateComponent } from '../update/employee-custom-update.component';
import { IEmployee } from '../employee-custom.model';
import { EmployeeCustomService } from '../service/employee-custom.service';

@Injectable({ providedIn: 'root' })
export class EmployeeResolve implements Resolve<IEmployee> {
  constructor(private service: EmployeeCustomService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employee: HttpResponse<IEmployee>) => {
          if (employee.body) {
            return of(employee.body);
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

export const EMPLOYEE_CUSTOM_ROUTE: Routes = [
  {
    path: '',
    component: EmployeeCustomComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.employeeCustom.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeCustomDetailComponent,
    resolve: {
      employee: EmployeeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeCustom.home.titleView',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeCustomUpdateComponent,
    resolve: {
      employee: EmployeeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeCustom.home.titleAdd',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeCustomUpdateComponent,
    resolve: {
      employee: EmployeeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeCustom.home.titleEdit',
    },
    canActivate: [UserRouteAccessService],
  },
];
