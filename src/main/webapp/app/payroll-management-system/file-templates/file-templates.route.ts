import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from '../../config/authority.constants';

import { UserFileTemplatesComponent } from './user-end/user-file-templates.component';
import { FileTemplates, IFileTemplates } from '../../shared/legacy/legacy-model/file-templates.model';
import { FileTemplatesService } from './file-templates.service';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class FileTemplatesResolve implements Resolve<IFileTemplates> {
  constructor(private service: FileTemplatesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileTemplates> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fileTemplates: HttpResponse<FileTemplates>) => {
          if (fileTemplates.body) {
            return of(fileTemplates.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileTemplates());
  }
}

export const fileTemplatesRoute: Routes = [
  {
    path: 'docs',
    component: UserFileTemplatesComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.userFiles.home',
    },
    canActivate: [UserRouteAccessService],
  },
];
