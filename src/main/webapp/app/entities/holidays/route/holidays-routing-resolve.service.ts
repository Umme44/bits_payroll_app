import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHolidays } from '../holidays.model';
import { HolidaysService } from '../service/holidays.service';

@Injectable({ providedIn: 'root' })
export class HolidaysRoutingResolveService implements Resolve<IHolidays | null> {
  constructor(protected service: HolidaysService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHolidays | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((holidays: HttpResponse<IHolidays>) => {
          if (holidays.body) {
            return of(holidays.body);
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
