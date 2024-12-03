import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOfficeNotices } from '../office-notices.model';
import { OfficeNoticesService } from '../service/office-notices.service';

@Injectable({ providedIn: 'root' })
export class OfficeNoticesRoutingResolveService implements Resolve<IOfficeNotices | null> {
  constructor(protected service: OfficeNoticesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfficeNotices | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((officeNotices: HttpResponse<IOfficeNotices>) => {
          if (officeNotices.body) {
            return of(officeNotices.body);
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
