import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalaryCertificate } from '../salary-certificate.model';
import { SalaryCertificateService } from '../service/salary-certificate.service';

@Injectable({ providedIn: 'root' })
export class SalaryCertificateRoutingResolveService implements Resolve<ISalaryCertificate | null> {
  constructor(protected service: SalaryCertificateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryCertificate | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salaryCertificate: HttpResponse<ISalaryCertificate>) => {
          if (salaryCertificate.body) {
            return of(salaryCertificate.body);
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
