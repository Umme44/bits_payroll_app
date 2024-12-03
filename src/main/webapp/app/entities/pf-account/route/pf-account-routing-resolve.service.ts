import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfAccount } from '../pf-account.model';
import { PfAccountService } from '../service/pf-account.service';

@Injectable({ providedIn: 'root' })
export class PfAccountRoutingResolveService implements Resolve<IPfAccount | null> {
  constructor(protected service: PfAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfAccount | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfAccount: HttpResponse<IPfAccount>) => {
          if (pfAccount.body) {
            return of(pfAccount.body);
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
