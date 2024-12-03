import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBand } from '../band.model';
import { BandService } from '../service/band.service';

@Injectable({ providedIn: 'root' })
export class BandRoutingResolveService implements Resolve<IBand | null> {
  constructor(protected service: BandService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBand | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((band: HttpResponse<IBand>) => {
          if (band.body) {
            return of(band.body);
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
