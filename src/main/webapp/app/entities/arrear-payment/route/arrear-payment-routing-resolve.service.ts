import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArrearPayment } from '../arrear-payment.model';
import { ArrearPaymentService } from '../service/arrear-payment.service';

@Injectable({ providedIn: 'root' })
export class ArrearPaymentRoutingResolveService implements Resolve<IArrearPayment | null> {
  constructor(protected service: ArrearPaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArrearPayment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arrearPayment: HttpResponse<IArrearPayment>) => {
          if (arrearPayment.body) {
            return of(arrearPayment.body);
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
