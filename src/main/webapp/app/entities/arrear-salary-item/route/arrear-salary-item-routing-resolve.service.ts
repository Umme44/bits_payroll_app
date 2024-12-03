import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { ArrearSalaryItemService } from '../service/arrear-salary-item.service';

@Injectable({ providedIn: 'root' })
export class ArrearSalaryItemRoutingResolveService implements Resolve<IArrearSalaryItem | null> {
  constructor(protected service: ArrearSalaryItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArrearSalaryItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arrearSalaryItem: HttpResponse<IArrearSalaryItem>) => {
          if (arrearSalaryItem.body) {
            return of(arrearSalaryItem.body);
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
