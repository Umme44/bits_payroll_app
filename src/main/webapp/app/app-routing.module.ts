import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        // custom modules
        // {
        //   path: 'dashboard',
        //   data: {
        //     authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        //   },
        //   canActivate: [UserRouteAccessService],
        //   loadChildren: () => import('./dashboard/dashboard.module').then(m => m.BitsHrPayrollDashboardModule),
        // },
        // {
        //   path: 'employee-address-book',
        //   canActivate: [UserRouteAccessService],
        //   component: EmployeeAddressBookComponent,
        //   data: {
        //     authorities: [Authority.USER],
        //     defaultSort: 'pin,asc',
        //     pageTitle: 'bitsHrPayrollApp.employee.home.titleAddress',
        //   },
        // },
        //
        // {
        //   path: 'blood-bank',
        //   canActivate: [UserRouteAccessService],
        //   component: BloodBankComponent,
        //   data: {
        //     authorities: [Authority.USER],
        //     defaultSort: 'id,asc',
        //     pageTitle: 'bitsHrPayrollApp.bloodBank.home.title',
        //   },
        // },
        //
        // {
        //   path: 'holiday-calendar',
        //   canActivate: [UserRouteAccessService],
        //   data: {
        //     authorities: [Authority.USER],
        //     defaultSort: 'id,asc',
        //     pageTitle: 'bitsHrPayrollApp.bloodBank.home.title',
        //   },
        //   loadChildren: () => import('./common/holiday-calendar/holiday-calendar.module').then(m => m.BitsHrPayrollAppHolidayCalendarModule),
        // },

        // {
        //   path: 'attendance-time-sheet-admin',
        //   canActivate: [UserRouteAccessService],
        //   component: AttendanceTimeSheetAdminComponent,
        //   data: {
        //     pageTitle: 'attendance-time-sheet.titleAdmin',
        //   },
        // },
        // {
        //   path: 'my-team/attendance-time-sheet',
        //   component: AttendanceTimeSheetMyTeamComponent,
        //   data: {
        //     pageType: 'myTeam',
        //     authorities: [],
        //     pageTitle: 'attendance-time-sheet.titleTeam',
        //   },
        //   canActivate: [UserRouteAccessService],
        // },
        // {
        //   path: 'my-team',
        //   canActivate: [UserRouteAccessService],
        //   component: MyTeamComponent,
        //   data: {
        //     pageTitle: 'bitsHrPayrollApp.my-team.home.title',
        //   },
        // },
        // {
        //   path: 'my-nominee',
        //   data: {
        //     authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
        //   },
        //   canActivate: [UserRouteAccessService],
        //   loadChildren: () =>
        //     import('./nominee-management-common/nominee-management.module').then(m => m.BitsHrPayrollNomineeManagementCommonModule),
        // },
        // custom modules

        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },

        // Admin Routes
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },

        // User Routes
        {
          path: '',
          loadChildren: () => import(`./user-routing.module`).then(m => m.UserRouterModule),
        },
        navbarRoute,
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
