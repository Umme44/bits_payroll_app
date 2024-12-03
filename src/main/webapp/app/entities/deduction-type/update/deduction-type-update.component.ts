import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DeductionTypeFormGroup, DeductionTypeFormService } from './deduction-type-form.service';
import { IDeductionType } from '../deduction-type.model';
import { DeductionTypeService } from '../service/deduction-type.service';

@Component({
  selector: 'jhi-deduction-type-update',
  templateUrl: './deduction-type-update.component.html',
})
export class DeductionTypeUpdateComponent implements OnInit {
  isSaving = false;
  deductionType: IDeductionType | null = null;

  editForm: DeductionTypeFormGroup = this.deductionTypeFormService.createDeductionTypeFormGroup();

  constructor(
    protected deductionTypeService: DeductionTypeService,
    protected deductionTypeFormService: DeductionTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deductionType }) => {
      this.deductionType = deductionType;
      if (deductionType) {
        this.updateForm(deductionType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deductionType = this.deductionTypeFormService.getDeductionType(this.editForm);
    if (deductionType.id !== null) {
      this.subscribeToSaveResponse(this.deductionTypeService.update(deductionType));
    } else {
      this.subscribeToSaveResponse(this.deductionTypeService.create(deductionType as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeductionType>>): void {
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

  protected updateForm(deductionType: IDeductionType): void {
    this.deductionType = deductionType;
    this.deductionTypeFormService.resetForm(this.editForm, deductionType);
  }
}
