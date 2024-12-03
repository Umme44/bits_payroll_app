import { Routes } from '@angular/router';
import { BatchOperationsComponent } from './batch-operations.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const BATCH_OPERATIONS_ROUTE: Routes = [
  {
    path: 'batch-operations',
    component: BatchOperationsComponent,
    data: {
      authorities: [],
      pageTitle: 'batch-operations.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
