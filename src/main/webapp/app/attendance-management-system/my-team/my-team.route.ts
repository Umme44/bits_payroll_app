import { Route } from '@angular/router';

import { MyTeamComponent } from './my-team.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import {
  AttendanceTimeSheetMyTeamComponent
} from "../ats/attendance-time-sheet-admin/attendance-time-sheet-my-team.component";

export const MY_TEAM_ROUTE = [
  {
    path: 'attendance-time-sheet',
    component: AttendanceTimeSheetMyTeamComponent,
    data: {
      pageType: 'myTeam',
      authorities: [],
      pageTitle: 'attendance-time-sheet.titleTeam',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: '',
    canActivate: [UserRouteAccessService],
    component: MyTeamComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.my-team.home.title',
    },
  }
  ];
