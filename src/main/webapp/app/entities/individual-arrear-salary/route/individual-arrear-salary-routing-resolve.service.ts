import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import { IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';

@Injectable({ providedIn: 'root' })
export class IndividualArrearSalaryRoutingResolveService implements Resolve<IIndividualArrearSalary | null> {
  constructor(protected service: IndividualArrearSalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIndividualArrearSalary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((individualArrearSalary: HttpResponse<IIndividualArrearSalary>) => {
          if (individualArrearSalary.body) {
            return of(individualArrearSalary.body);
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
