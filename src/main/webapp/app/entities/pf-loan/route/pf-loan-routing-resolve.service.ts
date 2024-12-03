import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfLoan } from '../pf-loan.model';
import { PfLoanService } from '../service/pf-loan.service';

@Injectable({ providedIn: 'root' })
export class PfLoanRoutingResolveService implements Resolve<IPfLoan | null> {
  constructor(protected service: PfLoanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfLoan | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfLoan: HttpResponse<IPfLoan>) => {
          if (pfLoan.body) {
            return of(pfLoan.body);
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
