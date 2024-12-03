import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { WorkingExperienceFormGroup, WorkingExperienceFormService } from './working-experience-form.service';
import { IWorkingExperience } from '../working-experience.model';
import { WorkingExperienceService } from '../service/working-experience.service';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-working-experience-update',
  templateUrl: './working-experience-update.component.html',
})
export class WorkingExperienceUpdateComponent implements OnInit {
  isSaving = false;
  workingExperience: IWorkingExperience | null = null;
  isInvalid = false;
  editForm: WorkingExperienceFormGroup = this.workingExperienceFormService.createWorkingExperienceFormGroup();

  constructor(
    protected workingExperienceService: WorkingExperienceService,
    protected workingExperienceFormService: WorkingExperienceFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingExperience }) => {
      this.workingExperience = workingExperience;
      if (workingExperience) {
        this.updateForm(workingExperience);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workingExperience = this.workingExperienceFormService.getWorkingExperience(this.editForm);
    if (workingExperience.id !== null) {
      this.subscribeToSaveResponse(this.workingExperienceService.update(workingExperience));
    } else {
      this.subscribeToSaveResponse(this.workingExperienceService.create(workingExperience as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingExperience>>): void {
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

  protected updateForm(workingExperience: IWorkingExperience): void {
    this.workingExperience = workingExperience;
    this.workingExperienceFormService.resetForm(this.editForm, workingExperience);
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
