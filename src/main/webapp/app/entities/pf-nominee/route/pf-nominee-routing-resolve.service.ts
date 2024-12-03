import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfNominee } from '../pf-nominee.model';
import { PfNomineeService } from '../service/pf-nominee.service';

@Injectable({ providedIn: 'root' })
export class PfNomineeRoutingResolveService implements Resolve<IPfNominee | null> {
  constructor(protected service: PfNomineeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfNominee | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfNominee: HttpResponse<IPfNominee>) => {
          if (pfNominee.body) {
            return of(pfNominee.body);
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
