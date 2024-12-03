import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmploymentHistory } from '../employment-history.model';
import { EmploymentHistoryService } from '../service/employment-history.service';

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryRoutingResolveService implements Resolve<IEmploymentHistory | null> {
  constructor(protected service: EmploymentHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employmentHistory: HttpResponse<IEmploymentHistory>) => {
          if (employmentHistory.body) {
            return of(employmentHistory.body);
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
