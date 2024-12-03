import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFlexSchedule } from '../flex-schedule.model';
import { FlexScheduleService } from '../service/flex-schedule.service';

@Injectable({ providedIn: 'root' })
export class FlexScheduleRoutingResolveService implements Resolve<IFlexSchedule | null> {
  constructor(protected service: FlexScheduleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlexSchedule | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((flexSchedule: HttpResponse<IFlexSchedule>) => {
          if (flexSchedule.body) {
            return of(flexSchedule.body);
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
