import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFloor } from '../floor.model';
import { FloorService } from '../service/floor.service';

@Injectable({ providedIn: 'root' })
export class FloorRoutingResolveService implements Resolve<IFloor | null> {
  constructor(protected service: FloorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFloor | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((floor: HttpResponse<IFloor>) => {
          if (floor.body) {
            return of(floor.body);
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
