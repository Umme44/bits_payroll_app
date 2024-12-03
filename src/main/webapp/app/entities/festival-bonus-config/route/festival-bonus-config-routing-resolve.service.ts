import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFestivalBonusConfig } from '../festival-bonus-config.model';
import { FestivalBonusConfigService } from '../service/festival-bonus-config.service';

@Injectable({ providedIn: 'root' })
export class FestivalBonusConfigRoutingResolveService implements Resolve<IFestivalBonusConfig | null> {
  constructor(protected service: FestivalBonusConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFestivalBonusConfig | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((festivalBonusConfig: HttpResponse<IFestivalBonusConfig>) => {
          if (festivalBonusConfig.body) {
            return of(festivalBonusConfig.body);
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
