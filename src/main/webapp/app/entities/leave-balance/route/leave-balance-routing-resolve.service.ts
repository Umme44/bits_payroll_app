import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveBalance } from '../leave-balance.model';
import { LeaveBalanceService } from '../service/leave-balance.service';

@Injectable({ providedIn: 'root' })
export class LeaveBalanceRoutingResolveService implements Resolve<ILeaveBalance | null> {
  constructor(protected service: LeaveBalanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveBalance | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaveBalance: HttpResponse<ILeaveBalance>) => {
          if (leaveBalance.body) {
            return of(leaveBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
