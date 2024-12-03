import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomRequisition } from '../room-requisition.model';
import { RoomRequisitionService } from '../service/room-requisition.service';

@Injectable({ providedIn: 'root' })
export class RoomRequisitionRoutingResolveService implements Resolve<IRoomRequisition | null> {
  constructor(protected service: RoomRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomRequisition | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomRequisition: HttpResponse<IRoomRequisition>) => {
          if (roomRequisition.body) {
            return of(roomRequisition.body);
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
