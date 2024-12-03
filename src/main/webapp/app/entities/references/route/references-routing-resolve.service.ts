import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReferences } from '../references.model';
import { ReferencesService } from '../service/references.service';

@Injectable({ providedIn: 'root' })
export class ReferencesRoutingResolveService implements Resolve<IReferences | null> {
  constructor(protected service: ReferencesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReferences | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((references: HttpResponse<IReferences>) => {
          if (references.body) {
            return of(references.body);
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
