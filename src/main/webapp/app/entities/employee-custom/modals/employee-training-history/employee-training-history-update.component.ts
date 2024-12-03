import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { swalOnRequestError, swalOnSavedSuccess, swalOnUpdatedSuccess } from 'app/shared/swal-common/swal-common';
import { IEmployee } from '../../employee-custom.model';
import { TrainingHistoryService } from '../../../training-history/service/training-history.service';
import { EmployeeCustomService } from '../../service/employee-custom.service';
import { ITrainingHistory } from '../../../training-history/training-history.model';
import { TrainingHistoryFormService } from '../../../training-history/update/training-history-form.service';

@Component({
  selector: 'jhi-employee-training-history-update',
  templateUrl: './employee-training-history-update.component.html',
})
export class EmployeeTrainingHistoryUpdateComponent implements OnInit, AfterViewInit, OnChanges {
  @Input()
  idForUpdate = -1;

  @Output()
  idForUpdateChange = new EventEmitter<number>();

  @Input()
  employeeId!: number;

  isSaving = false;
  employees: IEmployee[] = [];
  dateOfCompletionDp: any;

  editForm = this.employeeTrainingHistoryFormService.createTrainingHistoryFormGroup();

  constructor(
    protected trainingHistoryService: TrainingHistoryService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private employeeTrainingHistoryFormService: TrainingHistoryFormService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.idForUpdate !== -1) {
      this.trainingHistoryService.find(this.idForUpdate).subscribe(
        res => {
          this.updateForm(res.body!);
        },
        () => {
          swalOnRequestError();
        }
      );
    }
  }

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.activatedRoute.data.subscribe(({ trainingHistory }) => {
      this.updateForm(trainingHistory);

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(trainingHistory: ITrainingHistory): void {
    this.employeeTrainingHistoryFormService.resetForm(this.editForm, trainingHistory);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainingHistory = this.employeeTrainingHistoryFormService.getTrainingHistory(this.editForm);
    if (trainingHistory.id !== null && trainingHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingHistoryService.update(trainingHistory));
    } else {
      this.subscribeToSaveResponse(this.trainingHistoryService.create(trainingHistory));
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingHistory>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (!this.editForm.get('id')!.value) {
      swalOnSavedSuccess();
    } else {
      swalOnUpdatedSuccess();
    }
    this.editForm.reset();
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
    swalOnRequestError();
  }

  clear(): void {
    this.editForm.reset();
    //change to -1 for two-way data bind and activating ngOnChange
    this.idForUpdateChange.emit(-1);
  }
}
