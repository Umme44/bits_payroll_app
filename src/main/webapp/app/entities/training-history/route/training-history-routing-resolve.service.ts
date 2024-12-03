import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrainingHistory } from '../training-history.model';
import { TrainingHistoryService } from '../service/training-history.service';

@Injectable({ providedIn: 'root' })
export class TrainingHistoryRoutingResolveService implements Resolve<ITrainingHistory | null> {
  constructor(protected service: TrainingHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrainingHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trainingHistory: HttpResponse<ITrainingHistory>) => {
          if (trainingHistory.body) {
            return of(trainingHistory.body);
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
