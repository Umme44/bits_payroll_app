import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import { FestivalBonusDetailsService } from '../service/festival-bonus-details.service';

@Injectable({ providedIn: 'root' })
export class FestivalBonusDetailsRoutingResolveService implements Resolve<IFestivalBonusDetails | null> {
  constructor(protected service: FestivalBonusDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFestivalBonusDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((festivalBonusDetails: HttpResponse<IFestivalBonusDetails>) => {
          if (festivalBonusDetails.body) {
            return of(festivalBonusDetails.body);
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
