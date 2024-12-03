import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';

@Injectable({ providedIn: 'root' })
export class HoldFbDisbursementRoutingResolveService implements Resolve<IHoldFbDisbursement | null> {
  constructor(protected service: HoldFbDisbursementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHoldFbDisbursement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((holdFbDisbursement: HttpResponse<IHoldFbDisbursement>) => {
          if (holdFbDisbursement.body) {
            return of(holdFbDisbursement.body);
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
