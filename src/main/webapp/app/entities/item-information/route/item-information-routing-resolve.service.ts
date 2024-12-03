import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemInformation } from '../item-information.model';
import { ItemInformationService } from '../service/item-information.service';

@Injectable({ providedIn: 'root' })
export class ItemInformationRoutingResolveService implements Resolve<IItemInformation | null> {
  constructor(protected service: ItemInformationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemInformation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemInformation: HttpResponse<IItemInformation>) => {
          if (itemInformation.body) {
            return of(itemInformation.body);
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
