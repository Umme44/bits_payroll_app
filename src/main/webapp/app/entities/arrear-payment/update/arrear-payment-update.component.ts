import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ArrearPaymentFormGroup, ArrearPaymentFormService } from './arrear-payment-form.service';
import { IArrearPayment } from '../arrear-payment.model';
import { ArrearPaymentService } from '../service/arrear-payment.service';
import { IArrearSalaryItem } from 'app/entities/arrear-salary-item/arrear-salary-item.model';
import { ArrearSalaryItemService } from 'app/entities/arrear-salary-item/service/arrear-salary-item.service';
import { ArrearPaymentType } from 'app/entities/enumerations/arrear-payment-type.model';
import { Month } from 'app/entities/enumerations/month.model';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-arrear-payment-update',
  templateUrl: './arrear-payment-update.component.html',
})
export class ArrearPaymentUpdateComponent implements OnInit {
  isSaving = false;
  arrearPayment: IArrearPayment | null = null;
  arrearPaymentTypeValues = Object.keys(ArrearPaymentType);
  monthValues = Object.keys(Month);
  statusValues = Object.keys(Status);

  arrearSalaryItemsSharedCollection: IArrearSalaryItem[] = [];

  editForm: ArrearPaymentFormGroup = this.arrearPaymentFormService.createArrearPaymentFormGroup();

  constructor(
    protected arrearPaymentService: ArrearPaymentService,
    protected arrearPaymentFormService: ArrearPaymentFormService,
    protected arrearSalaryItemService: ArrearSalaryItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareArrearSalaryItem = (o1: IArrearSalaryItem | null, o2: IArrearSalaryItem | null): boolean =>
    this.arrearSalaryItemService.compareArrearSalaryItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearPayment }) => {
      this.arrearPayment = arrearPayment;
      if (arrearPayment) {
        this.updateForm(arrearPayment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arrearPayment = this.arrearPaymentFormService.getArrearPayment(this.editForm);
    if (arrearPayment.id !== null) {
      this.subscribeToSaveResponse(this.arrearPaymentService.update(arrearPayment));
    } else {
      this.subscribeToSaveResponse(this.arrearPaymentService.create(arrearPayment as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArrearPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(arrearPayment: IArrearPayment): void {
    this.arrearPayment = arrearPayment;
    this.arrearPaymentFormService.resetForm(this.editForm, arrearPayment);

    this.arrearSalaryItemsSharedCollection = this.arrearSalaryItemService.addArrearSalaryItemToCollectionIfMissing<IArrearSalaryItem>(
      this.arrearSalaryItemsSharedCollection,
      arrearPayment.arrearSalaryItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.arrearSalaryItemService
      .query()
      .pipe(map((res: HttpResponse<IArrearSalaryItem[]>) => res.body ?? []))
      .pipe(
        map((arrearSalaryItems: IArrearSalaryItem[]) =>
          this.arrearSalaryItemService.addArrearSalaryItemToCollectionIfMissing<IArrearSalaryItem>(
            arrearSalaryItems,
            this.arrearPayment?.arrearSalaryItem
          )
        )
      )
      .subscribe((arrearSalaryItems: IArrearSalaryItem[]) => (this.arrearSalaryItemsSharedCollection = arrearSalaryItems));
  }
}
