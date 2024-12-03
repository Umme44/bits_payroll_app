import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManualAttendanceEntry } from '../manual-attendance-entry.model';
import { ManualAttendanceEntryService } from '../service/manual-attendance-entry.service';

@Injectable({ providedIn: 'root' })
export class ManualAttendanceEntryRoutingResolveService implements Resolve<IManualAttendanceEntry | null> {
  constructor(protected service: ManualAttendanceEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManualAttendanceEntry | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manualAttendanceEntry: HttpResponse<IManualAttendanceEntry>) => {
          if (manualAttendanceEntry.body) {
            return of(manualAttendanceEntry.body);
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
