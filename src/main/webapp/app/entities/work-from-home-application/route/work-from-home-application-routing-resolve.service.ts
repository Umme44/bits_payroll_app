import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkFromHomeApplication } from '../work-from-home-application.model';
import { WorkFromHomeApplicationService } from '../service/work-from-home-application.service';

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationRoutingResolveService implements Resolve<IWorkFromHomeApplication | null> {
  constructor(protected service: WorkFromHomeApplicationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkFromHomeApplication | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workFromHomeApplication: HttpResponse<IWorkFromHomeApplication>) => {
          if (workFromHomeApplication.body) {
            return of(workFromHomeApplication.body);
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
