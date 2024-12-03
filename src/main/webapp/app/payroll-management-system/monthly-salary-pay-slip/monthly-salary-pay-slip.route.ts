import { Route } from '@angular/router';
import { MonthlySalaryPaySlipComponent } from './monthly-salary-pay-slip.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { EmployeeSalaryRoutingResolveService } from '../../entities/employee-salary/route/employee-salary-routing-resolve.service';

export const MONTHLY_SALARY_PAY_SLIP_ROUTE: Route = {
  path: ':id/monthly-salary-pay-slip',
  component: MonthlySalaryPaySlipComponent,
  resolve: {
    employeeSalary: EmployeeSalaryRoutingResolveService,
  },
  data: {
    authorities: [Authority.USER],
    pageTitle: 'bitsHrPayrollApp.employeeSalary.home.title',
  },
  canActivate: [UserRouteAccessService],
};
