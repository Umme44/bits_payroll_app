import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { IncomeTaxChallanService } from '../service/income-tax-challan.service';

@Injectable({ providedIn: 'root' })
export class IncomeTaxChallanRoutingResolveService implements Resolve<IIncomeTaxChallan | null> {
  constructor(protected service: IncomeTaxChallanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncomeTaxChallan | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((incomeTaxChallan: HttpResponse<IIncomeTaxChallan>) => {
          if (incomeTaxChallan.body) {
            return of(incomeTaxChallan.body);
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
