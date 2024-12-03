import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmploymentCertificate } from '../employment-certificate.model';
import { EmploymentCertificateService } from '../service/employment-certificate.service';

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateRoutingResolveService implements Resolve<IEmploymentCertificate | null> {
  constructor(protected service: EmploymentCertificateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentCertificate | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employmentCertificate: HttpResponse<IEmploymentCertificate>) => {
          if (employmentCertificate.body) {
            return of(employmentCertificate.body);
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
