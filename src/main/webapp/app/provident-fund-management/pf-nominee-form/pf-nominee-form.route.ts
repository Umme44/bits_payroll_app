import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { PfNomineeFormService } from './pf-nominee-form.service';
import { PfNomineeFormDetailComponent } from './pf-nominee-form-detail.component';
import { PfNomineeFormPrintDetailsComponent } from './print-format/pf-nominee-form-details-print-format';
import { IPfNominee, PfNominee } from '../../shared/legacy/legacy-model/pf-nominee.model';

@Injectable({ providedIn: 'root' })
export class PfNomineeFormResolve implements Resolve<IPfNominee> {
  constructor(private service: PfNomineeFormService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPfNominee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pfNominee: HttpResponse<PfNominee>) => {
          if (pfNominee.body) {
            return of(pfNominee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PfNominee());
  }
}

export const pfNomineeFormRoute: Routes = [
  // {
  //   path: '',
  //   component: PfNomineeFormCrudComponent,
  //   data: {
  //     authorities: [Authority.USER],
  //     defaultSort: 'id,asc',
  //     pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  {
    path: ':id/view',
    component: PfNomineeFormDetailComponent,
    resolve: {
      pfNominee: PfNomineeFormResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: 'new',
  //   component: PfNomineeFormCrudComponent,
  //   resolve: {
  //     pfNominee: PfNomineeFormResolve,
  //   },
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  {
    path: 'print',
    component: PfNomineeFormPrintDetailsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfNominee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
