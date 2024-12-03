import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecialShiftTiming } from '../special-shift-timing.model';
import { SpecialShiftTimingService } from '../service/special-shift-timing.service';

@Injectable({ providedIn: 'root' })
export class SpecialShiftTimingRoutingResolveService implements Resolve<ISpecialShiftTiming | null> {
  constructor(protected service: SpecialShiftTimingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecialShiftTiming | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((specialShiftTiming: HttpResponse<ISpecialShiftTiming>) => {
          if (specialShiftTiming.body) {
            return of(specialShiftTiming.body);
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
