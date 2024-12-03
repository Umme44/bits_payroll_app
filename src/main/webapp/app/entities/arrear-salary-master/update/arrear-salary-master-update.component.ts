import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ArrearSalaryMasterFormGroup, ArrearSalaryMasterFormService } from './arrear-salary-master-form.service';
import { IArrearSalaryMaster } from '../arrear-salary-master.model';
import { ArrearSalaryMasterService } from '../service/arrear-salary-master.service';

@Component({
  selector: 'jhi-arrear-salary-master-update',
  templateUrl: './arrear-salary-master-update.component.html',
})
export class ArrearSalaryMasterUpdateComponent implements OnInit {
  isSaving = false;
  arrearSalaryMaster: IArrearSalaryMaster | null = null;

  editForm: ArrearSalaryMasterFormGroup = this.arrearSalaryMasterFormService.createArrearSalaryMasterFormGroup();

  constructor(
    protected arrearSalaryMasterService: ArrearSalaryMasterService,
    protected arrearSalaryMasterFormService: ArrearSalaryMasterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalaryMaster }) => {
      this.arrearSalaryMaster = arrearSalaryMaster;
      if (arrearSalaryMaster) {
        this.updateForm(arrearSalaryMaster);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arrearSalaryMaster = this.arrearSalaryMasterFormService.getArrearSalaryMaster(this.editForm);
    if (arrearSalaryMaster.id !== null) {
      this.subscribeToSaveResponse(this.arrearSalaryMasterService.update(arrearSalaryMaster));
    } else {
      this.subscribeToSaveResponse(this.arrearSalaryMasterService.create(arrearSalaryMaster as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArrearSalaryMaster>>): void {
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

  protected updateForm(arrearSalaryMaster: IArrearSalaryMaster): void {
    this.arrearSalaryMaster = arrearSalaryMaster;
    this.arrearSalaryMasterFormService.resetForm(this.editForm, arrearSalaryMaster);
  }
}
