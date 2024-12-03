import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserFeedback } from '../user-feedback.model';
import { UserFeedbackService } from '../service/user-feedback.service';

@Injectable({ providedIn: 'root' })
export class UserFeedbackRoutingResolveService implements Resolve<IUserFeedback | null> {
  constructor(protected service: UserFeedbackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserFeedback | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userFeedback: HttpResponse<IUserFeedback>) => {
          if (userFeedback.body) {
            return of(userFeedback.body);
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
