import { Routes } from '@angular/router';
import {UserRouteAccessService} from "../../core/auth/user-route-access.service";
import {ArchiveOfferComponent} from "./archive-offer.component";

export const archiveOfferRoute: Routes = [
  {
    path: '',
    component: ArchiveOfferComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.offer.home.archive',
    },
    canActivate: [UserRouteAccessService],
  },
];
