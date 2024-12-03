import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeePin } from '../employee-pin.model';
import { EmployeePinService } from '../service/employee-pin.service';

@Injectable({ providedIn: 'root' })
export class EmployeePinRoutingResolveService implements Resolve<IEmployeePin | null> {
  constructor(protected service: EmployeePinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeePin | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeePin: HttpResponse<IEmployeePin>) => {
          if (employeePin.body) {
            return of(employeePin.body);
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
