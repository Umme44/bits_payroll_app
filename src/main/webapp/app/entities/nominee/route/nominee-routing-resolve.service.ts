import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INominee } from '../nominee.model';
import { NomineeService } from '../service/nominee.service';

@Injectable({ providedIn: 'root' })
export class NomineeRoutingResolveService implements Resolve<INominee | null> {
  constructor(protected service: NomineeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INominee | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nominee: HttpResponse<INominee>) => {
          if (nominee.body) {
            return of(nominee.body);
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
