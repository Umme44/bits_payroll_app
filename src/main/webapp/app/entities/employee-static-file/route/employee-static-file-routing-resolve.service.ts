import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeStaticFile } from '../employee-static-file.model';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';

@Injectable({ providedIn: 'root' })
export class EmployeeStaticFileRoutingResolveService implements Resolve<IEmployeeStaticFile | null> {
  constructor(protected service: EmployeeStaticFileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeStaticFile | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeStaticFile: HttpResponse<IEmployeeStaticFile>) => {
          if (employeeStaticFile.body) {
            return of(employeeStaticFile.body);
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
