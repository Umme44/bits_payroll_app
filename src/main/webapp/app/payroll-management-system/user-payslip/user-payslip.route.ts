import { Routes } from '@angular/router';
import { EmployeeSalaryRoutingResolveService } from '../../entities/employee-salary/route/employee-salary-routing-resolve.service';
import { UserPayslipComponent } from './user-payslip.component';
import { FestivalBonusPayslipComponent } from 'app/payroll-management-system/user-payslip/festival-bonus/festival-bonus-payslip.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { ArrearSlipComponent } from './arrear-slip/arrear-slip.component';

export const USER_PAYSLIP_ROUTES: Routes = [
  {
    path: 'my-payslip',
    component: UserPayslipComponent,
    resolve: {
      employeeSalary: EmployeeSalaryRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.payslip.home.monthlySalaryPayslip',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'my-arrear-payslip',
    component: ArrearSlipComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.payslip.home.arrearPayslip',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'my-festival-bonus-payslip',
    component: FestivalBonusPayslipComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.payslip.home.festivalPayslip',
    },
    canActivate: [UserRouteAccessService],
  },
];
