import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPfCollection } from '../pf-collection.model';
import { PfCollectionService } from '../service/pf-collection.service';

@Injectable({ providedIn: 'root' })
export class PfCollectionRoutingResolveService implements Resolve<IPfCollection | null> {
  constructor(protected service: PfCollectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfCollection | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pfCollection: HttpResponse<IPfCollection>) => {
          if (pfCollection.body) {
            return of(pfCollection.body);
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
