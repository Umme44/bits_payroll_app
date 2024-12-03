import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveAllocation } from '../leave-allocation.model';
import { LeaveAllocationService } from '../service/leave-allocation.service';

@Injectable({ providedIn: 'root' })
export class LeaveAllocationRoutingResolveService implements Resolve<ILeaveAllocation | null> {
  constructor(protected service: LeaveAllocationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveAllocation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaveAllocation: HttpResponse<ILeaveAllocation>) => {
          if (leaveAllocation.body) {
            return of(leaveAllocation.body);
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
