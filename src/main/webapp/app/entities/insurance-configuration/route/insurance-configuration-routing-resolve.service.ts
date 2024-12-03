import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsuranceConfiguration } from '../insurance-configuration.model';
import { InsuranceConfigurationService } from '../service/insurance-configuration.service';

@Injectable({ providedIn: 'root' })
export class InsuranceConfigurationRoutingResolveService implements Resolve<IInsuranceConfiguration | null> {
  constructor(protected service: InsuranceConfigurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInsuranceConfiguration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((insuranceConfiguration: HttpResponse<IInsuranceConfiguration>) => {
          if (insuranceConfiguration.body) {
            return of(insuranceConfiguration.body);
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
