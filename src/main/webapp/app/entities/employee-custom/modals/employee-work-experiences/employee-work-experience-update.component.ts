import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { swalOnRequestError, swalOnSavedSuccess, swalOnUpdatedSuccess } from 'app/shared/swal-common/swal-common';
import { WorkingExperienceFormService } from '../../../working-experience/update/working-experience-form.service';
import { IEmployee } from '../../employee-custom.model';
import { EmployeeCustomService } from '../../service/employee-custom.service';
import { WorkingExperienceService } from '../../../working-experience/service/working-experience.service';
import { IWorkingExperience } from '../../../working-experience/working-experience.model';

@Component({
  selector: 'jhi-employee-work-experience-update',
  templateUrl: './employee-work-experience-update.component.html',
})
export class EmployeeWorkExperienceUpdateComponent implements OnInit, OnChanges {
  @Input()
  idForUpdate = -1;

  @Input()
  employeeId!: number;

  isSaving = false;
  employees: IEmployee[] = [];
  dojOfLastOrganizationDp: any;
  dorOfLastOrganizationDp: any;
  isInvalid = false;

  editForm = this.workExperianceFormService.createWorkingExperienceFormGroup();

  constructor(
    protected workingExperienceService: WorkingExperienceService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected workExperianceFormService: WorkingExperienceFormService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.idForUpdate !== -1) {
      this.workingExperienceService.find(this.idForUpdate).subscribe(
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

  checkDate(): void {
    const doj = this.editForm.get(['dojOfLastOrganization'])!.value;
    const dor = this.editForm.get(['dorOfLastOrganization'])!.value;

    if (doj && dor && doj > dor) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  updateForm(workingExperience: IWorkingExperience): void {
    this.workExperianceFormService.resetForm(this.editForm, workingExperience);
  }

  clear(): void {
    this.editForm.reset();
  }

  save(): void {
    this.isSaving = true;
    const workingExperience = this.workExperianceFormService.getWorkingExperience(this.editForm);
    if (workingExperience.id !== null && workingExperience.id !== undefined) {
      this.subscribeToSaveResponse(this.workingExperienceService.update(workingExperience));
    } else {
      this.subscribeToSaveResponse(this.workingExperienceService.create(workingExperience));
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingExperience>>): void {
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
    this.clear();
  }

  protected onSaveError(): void {
    swalOnRequestError();
    this.isSaving = false;
  }
}
