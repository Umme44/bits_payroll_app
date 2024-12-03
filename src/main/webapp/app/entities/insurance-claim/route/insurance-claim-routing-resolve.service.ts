import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsuranceClaim } from '../insurance-claim.model';
import { InsuranceClaimService } from '../service/insurance-claim.service';

@Injectable({ providedIn: 'root' })
export class InsuranceClaimRoutingResolveService implements Resolve<IInsuranceClaim | null> {
  constructor(protected service: InsuranceClaimService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInsuranceClaim | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((insuranceClaim: HttpResponse<IInsuranceClaim>) => {
          if (insuranceClaim.body) {
            return of(insuranceClaim.body);
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
