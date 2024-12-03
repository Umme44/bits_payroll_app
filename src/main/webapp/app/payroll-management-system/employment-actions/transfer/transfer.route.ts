import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { TransferListComponent } from './transfer-list.component';
import { TransferDetailComponent } from './transfer-detail.component';
import { TransferComponent } from './transfer.component';
import { TransferUpdateComponent } from './transfer-update.component';
import { EmploymentHistory, IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { EmploymentHistoryService } from '../../../shared/legacy/legacy-service/employment-history.service';
import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryResolve implements Resolve<IEmploymentHistory> {
  constructor(private service: EmploymentHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employmentHistory: HttpResponse<EmploymentHistory>) => {
          if (employmentHistory.body) {
            return of(employmentHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmploymentHistory());
  }
}

export const TRANSFER_ROUTE: Routes = [
  {
    path: 'transfer',
    component: TransferListComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.transfer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'transfer/:id/view',
    component: TransferDetailComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.transfer.home.View',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'transfer/new',
    component: TransferComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.transfer.home.new',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'transfer/:id/edit',
    component: TransferUpdateComponent,
    resolve: {
      employmentHistory: EmploymentHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.transfer.home.edit',
    },
    canActivate: [UserRouteAccessService],
  },
];
