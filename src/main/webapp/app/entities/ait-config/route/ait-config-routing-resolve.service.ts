import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAitConfig } from '../ait-config.model';
import { AitConfigService } from '../service/ait-config.service';

@Injectable({ providedIn: 'root' })
export class AitConfigRoutingResolveService implements Resolve<IAitConfig | null> {
  constructor(protected service: AitConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAitConfig | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aitConfig: HttpResponse<IAitConfig>) => {
          if (aitConfig.body) {
            return of(aitConfig.body);
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
