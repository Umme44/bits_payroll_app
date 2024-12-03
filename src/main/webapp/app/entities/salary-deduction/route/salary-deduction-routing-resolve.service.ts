import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalaryDeduction } from '../salary-deduction.model';
import { SalaryDeductionService } from '../service/salary-deduction.service';

@Injectable({ providedIn: 'root' })
export class SalaryDeductionRoutingResolveService implements Resolve<ISalaryDeduction | null> {
  constructor(protected service: SalaryDeductionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryDeduction | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salaryDeduction: HttpResponse<ISalaryDeduction>) => {
          if (salaryDeduction.body) {
            return of(salaryDeduction.body);
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
