import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';

@Injectable({ providedIn: 'root' })
export class PfLoanRepaymentRoutingResolveService implements Resolve<IPfLoanRepayment | null> {
  constructor(protected service: PfLoanRepaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfLoanRepayment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfLoanRepayment: HttpResponse<IPfLoanRepayment>) => {
          if (pfLoanRepayment.body) {
            return of(pfLoanRepayment.body);
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
