import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import { ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';

@Injectable({ providedIn: 'root' })
export class ProRataFestivalBonusRoutingResolveService implements Resolve<IProRataFestivalBonus | null> {
  constructor(protected service: ProRataFestivalBonusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProRataFestivalBonus | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((proRataFestivalBonus: HttpResponse<IProRataFestivalBonus>) => {
          if (proRataFestivalBonus.body) {
            return of(proRataFestivalBonus.body);
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
