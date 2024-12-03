import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SalaryGeneratorMasterFormService, SalaryGeneratorMasterFormGroup } from './salary-generator-master-form.service';
import { ISalaryGeneratorMaster } from '../salary-generator-master.model';
import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';
import { Visibility } from 'app/entities/enumerations/visibility.model';

@Component({
  selector: 'jhi-salary-generator-master-update',
  templateUrl: './salary-generator-master-update.component.html',
})
export class SalaryGeneratorMasterUpdateComponent implements OnInit {
  isSaving = false;
  salaryGeneratorMaster: ISalaryGeneratorMaster | null = null;
  visibilityValues = Object.keys(Visibility);

  editForm: SalaryGeneratorMasterFormGroup = this.salaryGeneratorMasterFormService.createSalaryGeneratorMasterFormGroup();

  constructor(
    protected salaryGeneratorMasterService: SalaryGeneratorMasterService,
    protected salaryGeneratorMasterFormService: SalaryGeneratorMasterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryGeneratorMaster }) => {
      this.salaryGeneratorMaster = salaryGeneratorMaster;
      if (salaryGeneratorMaster) {
        this.updateForm(salaryGeneratorMaster);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryGeneratorMaster = this.salaryGeneratorMasterFormService.getSalaryGeneratorMaster(this.editForm);
    if (salaryGeneratorMaster.id !== null) {
      this.subscribeToSaveResponse(this.salaryGeneratorMasterService.update(salaryGeneratorMaster));
    } else {
      this.subscribeToSaveResponse(this.salaryGeneratorMasterService.create(salaryGeneratorMaster as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryGeneratorMaster>>): void {
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

  protected updateForm(salaryGeneratorMaster: ISalaryGeneratorMaster): void {
    this.salaryGeneratorMaster = salaryGeneratorMaster;
    this.salaryGeneratorMasterFormService.resetForm(this.editForm, salaryGeneratorMaster);
  }
}
