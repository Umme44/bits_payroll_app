import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeResignation } from '../employee-resignation.model';
import { EmployeeResignationService } from '../service/employee-resignation.service';

@Injectable({ providedIn: 'root' })
export class EmployeeResignationRoutingResolveService implements Resolve<IEmployeeResignation | null> {
  constructor(protected service: EmployeeResignationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeResignation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeResignation: HttpResponse<IEmployeeResignation>) => {
          if (employeeResignation.body) {
            return of(employeeResignation.body);
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
