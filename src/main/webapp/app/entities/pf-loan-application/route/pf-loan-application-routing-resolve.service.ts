import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfLoanApplication } from '../pf-loan-application.model';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationRoutingResolveService implements Resolve<IPfLoanApplication | null> {
  constructor(protected service: PfLoanApplicationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfLoanApplication | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfLoanApplication: HttpResponse<IPfLoanApplication>) => {
          if (pfLoanApplication.body) {
            return of(pfLoanApplication.body);
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
