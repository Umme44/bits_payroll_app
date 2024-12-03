import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';

@Injectable({ providedIn: 'root' })
export class EventLogRoutingResolveService implements Resolve<IEventLog | null> {
  constructor(protected service: EventLogService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventLog | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventLog: HttpResponse<IEventLog>) => {
          if (eventLog.body) {
            return of(eventLog.body);
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
