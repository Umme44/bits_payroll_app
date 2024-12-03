import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../service/recruitment-requisition-form.service';

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionFormRoutingResolveService implements Resolve<IRecruitmentRequisitionForm | null> {
  constructor(protected service: RecruitmentRequisitionFormService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecruitmentRequisitionForm | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recruitmentRequisitionForm: HttpResponse<IRecruitmentRequisitionForm>) => {
          if (recruitmentRequisitionForm.body) {
            return of(recruitmentRequisitionForm.body);
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
