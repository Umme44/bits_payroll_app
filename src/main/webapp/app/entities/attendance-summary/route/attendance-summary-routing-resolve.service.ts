import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendanceSummary } from '../attendance-summary.model';
import { AttendanceSummaryService } from '../service/attendance-summary.service';

@Injectable({ providedIn: 'root' })
export class AttendanceSummaryRoutingResolveService implements Resolve<IAttendanceSummary | null> {
  constructor(protected service: AttendanceSummaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendanceSummary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attendanceSummary: HttpResponse<IAttendanceSummary>) => {
          if (attendanceSummary.body) {
            return of(attendanceSummary.body);
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
