import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalaryGeneratorMaster } from '../salary-generator-master.model';
import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';

@Injectable({ providedIn: 'root' })
export class SalaryGeneratorMasterRoutingResolveService implements Resolve<ISalaryGeneratorMaster | null> {
  constructor(protected service: SalaryGeneratorMasterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalaryGeneratorMaster | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salaryGeneratorMaster: HttpResponse<ISalaryGeneratorMaster>) => {
          if (salaryGeneratorMaster.body) {
            return of(salaryGeneratorMaster.body);
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
