import { ActivatedRouteSnapshot, Resolve, Route, Router } from '@angular/router';

import { MonthlySalaryPaySlipComponent } from './monthly-salary-pay-slip.component';
import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { EmployeeSalaryService } from '../../employee-salary/service/employee-salary.service';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { EmployeeSalary, IEmployeeSalary } from '../../employee-salary/employee-salary.model';

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryResolve implements Resolve<IEmployeeSalary> {
  constructor(private service: EmployeeSalaryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeSalary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap(employeeSalary => {
          if (employeeSalary.body) {
            return of(employeeSalary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeSalary());
  }
}

export const MONTHLY_SALARY_PAY_SLIP_ROUTE: Route = {
  path: ':id/monthly-salary-pay-slip',
  component: MonthlySalaryPaySlipComponent,
  resolve: {
    employeeSalary: EmployeeSalaryResolve,
  },
  data: {
    authorities: [Authority.USER],
    pageTitle: 'bitsHrPayrollApp.employeeSalary.home.title',
  },
  canActivate: [UserRouteAccessService],
};
