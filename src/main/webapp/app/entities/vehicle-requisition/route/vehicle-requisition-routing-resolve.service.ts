import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleRequisition } from '../vehicle-requisition.model';
import { VehicleRequisitionService } from '../service/vehicle-requisition.service';

@Injectable({ providedIn: 'root' })
export class VehicleRequisitionRoutingResolveService implements Resolve<IVehicleRequisition | null> {
  constructor(protected service: VehicleRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVehicleRequisition | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vehicleRequisition: HttpResponse<IVehicleRequisition>) => {
          if (vehicleRequisition.body) {
            return of(vehicleRequisition.body);
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
