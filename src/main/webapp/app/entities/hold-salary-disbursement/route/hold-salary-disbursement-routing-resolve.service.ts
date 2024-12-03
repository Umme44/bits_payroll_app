import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';
import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';

@Injectable({ providedIn: 'root' })
export class HoldSalaryDisbursementRoutingResolveService implements Resolve<IHoldSalaryDisbursement | null> {
  constructor(protected service: HoldSalaryDisbursementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHoldSalaryDisbursement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((holdSalaryDisbursement: HttpResponse<IHoldSalaryDisbursement>) => {
          if (holdSalaryDisbursement.body) {
            return of(holdSalaryDisbursement.body);
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
