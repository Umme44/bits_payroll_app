import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserTaxAcknowledgementReceiptService } from '../service/user-tax-acknowledgement-receipt.service';
import { UserTaxAcknowledgementReceiptComponent } from '../list/user-tax-acknowledgement-receipt.component';
import { UserTaxAcknowledgementReceiptDetailComponent } from '../detail/user-tax-acknowledgement-receipt-detail.component';
import { UserTaxAcknowledgementReceiptUpdateComponent } from '../update/user-tax-acknowledgement-receipt-update.component';
import {ITaxAcknowledgementReceipt, NewTaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {UserRouteAccessService} from "../../../core/auth/user-route-access.service";
@Injectable({ providedIn: 'root' })
export class TaxAcknowledgementReceiptResolve implements Resolve<ITaxAcknowledgementReceipt> {
  constructor(private service: UserTaxAcknowledgementReceiptService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxAcknowledgementReceipt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((taxAcknowledgementReceipt: HttpResponse<NewTaxAcknowledgementReceipt>) => {
          if (taxAcknowledgementReceipt.body) {
            return of(taxAcknowledgementReceipt.body);
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

export const userTaxAcknowledgementReceiptRoute: Routes = [
  {
    path: '',
    component: UserTaxAcknowledgementReceiptComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserTaxAcknowledgementReceiptDetailComponent,
    resolve: {
      taxAcknowledgementReceipt: TaxAcknowledgementReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserTaxAcknowledgementReceiptUpdateComponent,
    resolve: {
      taxAcknowledgementReceipt: TaxAcknowledgementReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserTaxAcknowledgementReceiptUpdateComponent,
    resolve: {
      taxAcknowledgementReceipt: TaxAcknowledgementReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];
