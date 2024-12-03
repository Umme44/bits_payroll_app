import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArrearSalary } from '../arrear-salary.model';
import { ArrearSalaryService } from '../service/arrear-salary.service';

@Injectable({ providedIn: 'root' })
export class ArrearSalaryRoutingResolveService implements Resolve<IArrearSalary | null> {
  constructor(protected service: ArrearSalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArrearSalary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arrearSalary: HttpResponse<IArrearSalary>) => {
          if (arrearSalary.body) {
            return of(arrearSalary.body);
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
