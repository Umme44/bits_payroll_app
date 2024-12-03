import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFestival } from '../festival.model';
import { FestivalService } from '../service/festival.service';

@Injectable({ providedIn: 'root' })
export class FestivalRoutingResolveService implements Resolve<IFestival | null> {
  constructor(protected service: FestivalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFestival | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((festival: HttpResponse<IFestival>) => {
          if (festival.body) {
            return of(festival.body);
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
