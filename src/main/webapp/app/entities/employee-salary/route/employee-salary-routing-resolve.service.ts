import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeSalary } from '../employee-salary.model';
import { EmployeeSalaryService } from '../service/employee-salary.service';

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryRoutingResolveService implements Resolve<IEmployeeSalary | null> {
  constructor(protected service: EmployeeSalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeSalary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeSalary: HttpResponse<IEmployeeSalary>) => {
          if (employeeSalary.body) {
            return of(employeeSalary.body);
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
