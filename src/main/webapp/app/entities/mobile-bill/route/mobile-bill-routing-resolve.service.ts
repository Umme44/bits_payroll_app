import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMobileBill } from '../mobile-bill.model';
import { MobileBillService } from '../service/mobile-bill.service';

@Injectable({ providedIn: 'root' })
export class MobileBillRoutingResolveService implements Resolve<IMobileBill | null> {
  constructor(protected service: MobileBillService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMobileBill | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mobileBill: HttpResponse<IMobileBill>) => {
          if (mobileBill.body) {
            return of(mobileBill.body);
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
