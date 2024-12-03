import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';

@Injectable({ providedIn: 'root' })
export class InsuranceRegistrationRoutingResolveService implements Resolve<IInsuranceRegistration | null> {
  constructor(protected service: InsuranceRegistrationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInsuranceRegistration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((insuranceRegistration: HttpResponse<IInsuranceRegistration>) => {
          if (insuranceRegistration.body) {
            return of(insuranceRegistration.body);
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
