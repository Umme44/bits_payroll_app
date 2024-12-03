import { Routes } from '@angular/router';
import {UserRouteAccessService} from "../../core/auth/user-route-access.service";
import {ArchiveOfficeNoticesComponent} from "./archive-office-notices.component";

export const archiveOfficeNoticesRoute: Routes = [
  {
    path: '',
    component: ArchiveOfficeNoticesComponent,
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.officeNotices.home.archive',
    },
    canActivate: [UserRouteAccessService],
  },
];
