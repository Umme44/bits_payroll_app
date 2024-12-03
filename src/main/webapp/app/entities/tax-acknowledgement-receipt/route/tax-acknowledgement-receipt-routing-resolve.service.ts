import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { TaxAcknowledgementReceiptService } from '../service/tax-acknowledgement-receipt.service';
import {
  ITaxAcknowledgementReceipt
} from "../../../payroll-management-system/user-tax-acknowledgement-receipt/tax-acknowledgement-receipt.model";

@Injectable({ providedIn: 'root' })
export class TaxAcknowledgementReceiptRoutingResolveService implements Resolve<ITaxAcknowledgementReceipt | null> {
  constructor(protected service: TaxAcknowledgementReceiptService, protected router: Router) {}

  // resolve(route: ActivatedRouteSnapshot): Observable<ITaxAcknowledgementReceipt | null | never> {
  //   const id = route.params['id'];
  //   if (id) {
  //     return this.service.find(id).pipe(
  //       mergeMap((taxAcknowledgementReceipt: HttpResponse<ITaxAcknowledgementReceipt>) => {
  //         if (taxAcknowledgementReceipt.body) {
  //           return of(taxAcknowledgementReceipt.body);
  //         } else {
  //           this.router.navigate(['404']);
  //           return EMPTY;
  //         }
  //       })
  //     );
  //   }
  //   return of(null);
  // }

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxAcknowledgementReceipt> | Observable<never> {
    const id = route.params['id'];
    const employeeId = route.params['employeeId'];
    if (id) {
      return this.service.findV2(id, employeeId).pipe(
        mergeMap((taxAcknowledgementReceipt: HttpResponse<ITaxAcknowledgementReceipt>) => {
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
