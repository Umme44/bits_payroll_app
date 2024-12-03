import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkingExperience } from '../working-experience.model';
import { WorkingExperienceService } from '../service/working-experience.service';

@Injectable({ providedIn: 'root' })
export class WorkingExperienceRoutingResolveService implements Resolve<IWorkingExperience | null> {
  constructor(protected service: WorkingExperienceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkingExperience | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workingExperience: HttpResponse<IWorkingExperience>) => {
          if (workingExperience.body) {
            return of(workingExperience.body);
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
