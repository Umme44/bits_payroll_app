import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendanceSyncCache } from '../attendance-sync-cache.model';
import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';

@Injectable({ providedIn: 'root' })
export class AttendanceSyncCacheRoutingResolveService implements Resolve<IAttendanceSyncCache | null> {
  constructor(protected service: AttendanceSyncCacheService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendanceSyncCache | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attendanceSyncCache: HttpResponse<IAttendanceSyncCache>) => {
          if (attendanceSyncCache.body) {
            return of(attendanceSyncCache.body);
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
