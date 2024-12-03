import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcReq } from '../proc-req.model';
import { ProcReqService } from '../service/proc-req.service';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';

@Injectable({ providedIn: 'root' })
export class ProcReqRoutingResolveService implements Resolve<IProcReqMaster | null> {
  constructor(protected service: ProcReqService, protected router: Router) {}

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

    // const id = route.params['id'];
    // if (id) {
    //   return this.service.find(id).pipe(
    //     mergeMap((procReq: HttpResponse<IProcReq>) => {
    //       if (procReq.body) {
    //         return of(procReq.body);
    //       } else {
    //         this.router.navigate(['404']);
    //         return EMPTY;
    //       }
    //     })
    //   );
    // }
    // return of(null);
  }
}
