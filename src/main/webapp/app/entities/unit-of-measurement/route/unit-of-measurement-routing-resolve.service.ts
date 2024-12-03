import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';

@Injectable({ providedIn: 'root' })
export class UnitOfMeasurementRoutingResolveService implements Resolve<IUnitOfMeasurement | null> {
  constructor(protected service: UnitOfMeasurementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUnitOfMeasurement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((unitOfMeasurement: HttpResponse<IUnitOfMeasurement>) => {
          if (unitOfMeasurement.body) {
            return of(unitOfMeasurement.body);
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
