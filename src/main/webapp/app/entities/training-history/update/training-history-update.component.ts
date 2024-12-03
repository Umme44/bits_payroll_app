import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TrainingHistoryFormGroup, TrainingHistoryFormService } from './training-history-form.service';
import { ITrainingHistory } from '../training-history.model';
import { TrainingHistoryService } from '../service/training-history.service';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-training-history-update',
  templateUrl: './training-history-update.component.html',
})
export class TrainingHistoryUpdateComponent implements OnInit {
  isSaving = false;
  trainingHistory: ITrainingHistory | null = null;
  isInvalid = false;

  editForm: TrainingHistoryFormGroup = this.trainingHistoryFormService.createTrainingHistoryFormGroup();

  constructor(
    protected trainingHistoryService: TrainingHistoryService,
    protected trainingHistoryFormService: TrainingHistoryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingHistory }) => {
      this.trainingHistory = trainingHistory;
      if (trainingHistory) {
        this.updateForm(trainingHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainingHistory = this.trainingHistoryFormService.getTrainingHistory(this.editForm);
    if (trainingHistory.id !== null) {
      this.subscribeToSaveResponse(this.trainingHistoryService.update(trainingHistory));
    } else {
      this.subscribeToSaveResponse(this.trainingHistoryService.create(trainingHistory as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingHistory>>): void {
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

  protected updateForm(trainingHistory: ITrainingHistory): void {
    this.trainingHistory = trainingHistory;
    this.trainingHistoryFormService.resetForm(this.editForm, trainingHistory);
  }

  changeEmployee(employee: IEmployee): void {
    const deptHeadFormControl = this.editForm.get(['employeeId'])!;
    deptHeadFormControl.markAsTouched();
    if (employee) {
      deptHeadFormControl.setValue(employee.id);
    } else {
      deptHeadFormControl.setValue(null);
    }
  }

  checkDate(): void {
    const doj = this.editForm.get(['dojOfLastOrganization'])!.value;
    const dor = this.editForm.get(['dorOfLastOrganization'])!.value;

    if (doj && dor && doj > dor) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }
}
