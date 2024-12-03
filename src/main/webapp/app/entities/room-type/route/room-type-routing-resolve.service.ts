import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomType } from '../room-type.model';
import { RoomTypeService } from '../service/room-type.service';

@Injectable({ providedIn: 'root' })
export class RoomTypeRoutingResolveService implements Resolve<IRoomType | null> {
  constructor(protected service: RoomTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomType: HttpResponse<IRoomType>) => {
          if (roomType.body) {
            return of(roomType.body);
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
