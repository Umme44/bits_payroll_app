import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserWorkFromHomeApplicationService } from '../service/user-work-from-home-application.service';
import { IUserWorkFromHomeApplication, UserWorkFromHomeApplication } from '../user-work-from-home-application.model';

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationResolve implements Resolve<IUserWorkFromHomeApplication> {
  constructor(private service: UserWorkFromHomeApplicationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserWorkFromHomeApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((workFromHomeApplication: HttpResponse<UserWorkFromHomeApplication>) => {
          if (workFromHomeApplication.body) {
            return of(workFromHomeApplication.body);
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
