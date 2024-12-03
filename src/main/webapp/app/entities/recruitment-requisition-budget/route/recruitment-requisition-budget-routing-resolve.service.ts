import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionBudgetRoutingResolveService implements Resolve<IRecruitmentRequisitionBudget | null> {
  constructor(protected service: RecruitmentRequisitionBudgetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecruitmentRequisitionBudget | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recruitmentRequisitionBudget: HttpResponse<IRecruitmentRequisitionBudget>) => {
          if (recruitmentRequisitionBudget.body) {
            return of(recruitmentRequisitionBudget.body);
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
