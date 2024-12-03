import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeNOC } from '../employee-noc.model';
import { EmployeeNOCService } from '../service/employee-noc.service';

@Injectable({ providedIn: 'root' })
export class EmployeeNOCRoutingResolveService implements Resolve<IEmployeeNOC | null> {
  constructor(protected service: EmployeeNOCService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeNOC | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeNOC: HttpResponse<IEmployeeNOC>) => {
          if (employeeNOC.body) {
            return of(employeeNOC.body);
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
