import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArrearSalaryMaster } from '../arrear-salary-master.model';
import { ArrearSalaryMasterService } from '../service/arrear-salary-master.service';

@Injectable({ providedIn: 'root' })
export class ArrearSalaryMasterRoutingResolveService implements Resolve<IArrearSalaryMaster | null> {
  constructor(protected service: ArrearSalaryMasterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArrearSalaryMaster | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arrearSalaryMaster: HttpResponse<IArrearSalaryMaster>) => {
          if (arrearSalaryMaster.body) {
            return of(arrearSalaryMaster.body);
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
