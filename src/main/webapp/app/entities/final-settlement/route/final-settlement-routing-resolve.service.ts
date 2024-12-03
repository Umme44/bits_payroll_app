import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinalSettlement } from '../final-settlement.model';
import { FinalSettlementService } from '../service/final-settlement.service';

@Injectable({ providedIn: 'root' })
export class FinalSettlementRoutingResolveService implements Resolve<IFinalSettlement | null> {
  constructor(protected service: FinalSettlementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFinalSettlement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((finalSettlement: HttpResponse<IFinalSettlement>) => {
          if (finalSettlement.body) {
            return of(finalSettlement.body);
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
