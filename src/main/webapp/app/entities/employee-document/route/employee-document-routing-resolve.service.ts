import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeDocument } from '../employee-document.model';
import { EmployeeDocumentService } from '../service/employee-document.service';

@Injectable({ providedIn: 'root' })
export class EmployeeDocumentRoutingResolveService implements Resolve<IEmployeeDocument | null> {
  constructor(protected service: EmployeeDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeDocument | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeDocument: HttpResponse<IEmployeeDocument>) => {
          if (employeeDocument.body) {
            return of(employeeDocument.body);
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
