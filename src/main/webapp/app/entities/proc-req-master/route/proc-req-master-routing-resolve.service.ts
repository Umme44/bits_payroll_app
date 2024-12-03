import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcReqMaster } from '../proc-req-master.model';
import { ProcReqMasterService } from '../service/proc-req-master.service';

@Injectable({ providedIn: 'root' })
export class ProcReqMasterRoutingResolveService implements Resolve<IProcReqMaster | null> {
  constructor(protected service: ProcReqMasterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProcReqMaster | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((procReqMaster: HttpResponse<IProcReqMaster>) => {
          if (procReqMaster.body) {
            return of(procReqMaster.body);
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
