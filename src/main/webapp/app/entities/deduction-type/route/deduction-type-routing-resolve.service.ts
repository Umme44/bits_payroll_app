import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeductionType } from '../deduction-type.model';
import { DeductionTypeService } from '../service/deduction-type.service';

@Injectable({ providedIn: 'root' })
export class DeductionTypeRoutingResolveService implements Resolve<IDeductionType | null> {
  constructor(protected service: DeductionTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeductionType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deductionType: HttpResponse<IDeductionType>) => {
          if (deductionType.body) {
            return of(deductionType.body);
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
