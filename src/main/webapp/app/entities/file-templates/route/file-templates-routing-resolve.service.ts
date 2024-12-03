import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileTemplates } from '../file-templates.model';
import { FileTemplatesService } from '../service/file-templates.service';

@Injectable({ providedIn: 'root' })
export class FileTemplatesRoutingResolveService implements Resolve<IFileTemplates | null> {
  constructor(protected service: FileTemplatesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileTemplates | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileTemplates: HttpResponse<IFileTemplates>) => {
          if (fileTemplates.body) {
            return of(fileTemplates.body);
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
