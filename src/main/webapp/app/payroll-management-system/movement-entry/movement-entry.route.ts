import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

import { IMovementEntry, MovementEntry } from 'app/shared/legacy/legacy-model/movement-entry.model';
import { MovementEntryService } from './movement-entry.service';
import { UserMovementEntryComponent } from './user-movement-entry.component';
import { MovementEntryApprovalComponent } from './movement-entry-approval.component';

@Injectable({ providedIn: 'root' })
export class MovementEntryResolve implements Resolve<IMovementEntry> {
  constructor(private service: MovementEntryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMovementEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((movementEntry: HttpResponse<MovementEntry>) => {
          if (movementEntry.body) {
            return of(movementEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MovementEntry());
  }
}

export const movementEntryRoute: Routes = [
  // {
  //   path: '',
  //   component: MovementEntryComponent,
  //   data: {
  //     authorities: [Authority.USER],
  //     defaultSort: 'id,asc',
  //     pageTitle: 'bitsHrPayrollApp.movementEntry.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  {
    path: 'apply',
    component: UserMovementEntryComponent,
    resolve: {
      movementEntry: MovementEntryResolve,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.movementEntryUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   component: MovementEntryDetailComponent,
  //   resolve: {
  //     movementEntry: MovementEntryResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.movementEntry.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: MovementEntryUpdateComponent,
  //   resolve: {
  //     movementEntry: MovementEntryResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.movementEntry.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   component: MovementEntryUpdateComponent,
  //   resolve: {
  //     movementEntry: MovementEntryResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.movementEntry.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'approval/hr',
  //   component: MovementEntryApprovalComponent,
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.movementEntryAdmin.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  {
    path: 'approval/lm',
    component: MovementEntryApprovalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.movementEntry.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
