import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';
import { EmployeeSalaryTempDataService } from '../service/employee-salary-temp-data.service';

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryTempDataRoutingResolveService implements Resolve<IEmployeeSalaryTempData | null> {
  constructor(protected service: EmployeeSalaryTempDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeSalaryTempData | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeSalaryTempData: HttpResponse<IEmployeeSalaryTempData>) => {
          if (employeeSalaryTempData.body) {
            return of(employeeSalaryTempData.body);
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
