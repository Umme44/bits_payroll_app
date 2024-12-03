import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEducationDetails } from '../education-details.model';
import { EducationDetailsService } from '../service/education-details.service';

@Injectable({ providedIn: 'root' })
export class EducationDetailsRoutingResolveService implements Resolve<IEducationDetails | null> {
  constructor(protected service: EducationDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEducationDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((educationDetails: HttpResponse<IEducationDetails>) => {
          if (educationDetails.body) {
            return of(educationDetails.body);
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
