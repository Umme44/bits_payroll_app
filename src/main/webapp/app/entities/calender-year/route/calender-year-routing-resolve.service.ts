import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICalenderYear } from '../calender-year.model';
import { CalenderYearService } from '../service/calender-year.service';

@Injectable({ providedIn: 'root' })
export class CalenderYearRoutingResolveService implements Resolve<ICalenderYear | null> {
  constructor(protected service: CalenderYearService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalenderYear | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((calenderYear: HttpResponse<ICalenderYear>) => {
          if (calenderYear.body) {
            return of(calenderYear.body);
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
