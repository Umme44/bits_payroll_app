import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFlexScheduleApplication } from '../flex-schedule-application.model';
import { FlexScheduleApplicationService } from '../service/flex-schedule-application.service';

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationRoutingResolveService implements Resolve<IFlexScheduleApplication | null> {
  constructor(protected service: FlexScheduleApplicationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlexScheduleApplication | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((flexScheduleApplication: HttpResponse<IFlexScheduleApplication>) => {
          if (flexScheduleApplication.body) {
            return of(flexScheduleApplication.body);
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
