import { Routes } from '@angular/router';
import { SalaryPayslipAdminComponent } from './salary-payslip-admin.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { Authority } from '../../config/authority.constants';
import { EmployeeSalaryRoutingResolveService } from '../../entities/employee-salary/route/employee-salary-routing-resolve.service';

export const SALARY_PAYSLIP_ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: SalaryPayslipAdminComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.payslip.home.monthlySalaryPayslip',
    },
    canActivate: [UserRouteAccessService],
  },
];
