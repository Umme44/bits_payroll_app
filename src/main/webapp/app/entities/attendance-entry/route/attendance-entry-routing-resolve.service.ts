import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendanceEntry } from '../attendance-entry.model';
import { AttendanceEntryService } from '../service/attendance-entry.service';

@Injectable({ providedIn: 'root' })
export class AttendanceEntryRoutingResolveService implements Resolve<IAttendanceEntry | null> {
  constructor(protected service: AttendanceEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendanceEntry | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attendanceEntry: HttpResponse<IAttendanceEntry>) => {
          if (attendanceEntry.body) {
            return of(attendanceEntry.body);
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
