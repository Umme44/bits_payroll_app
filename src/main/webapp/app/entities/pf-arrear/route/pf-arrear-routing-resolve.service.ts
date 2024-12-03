import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfArrear } from '../pf-arrear.model';
import { PfArrearService } from '../service/pf-arrear.service';

@Injectable({ providedIn: 'root' })
export class PfArrearRoutingResolveService implements Resolve<IPfArrear | null> {
  constructor(protected service: PfArrearService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfArrear | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfArrear: HttpResponse<IPfArrear>) => {
          if (pfArrear.body) {
            return of(pfArrear.body);
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
