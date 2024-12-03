import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAitPayment } from '../ait-payment.model';
import { AitPaymentService } from '../service/ait-payment.service';

@Injectable({ providedIn: 'root' })
export class AitPaymentRoutingResolveService implements Resolve<IAitPayment | null> {
  constructor(protected service: AitPaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAitPayment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aitPayment: HttpResponse<IAitPayment>) => {
          if (aitPayment.body) {
            return of(aitPayment.body);
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
