import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ReferencesFormGroup, ReferencesFormService } from './references-form.service';
import { IReferences } from '../references.model';
import { ReferencesService } from '../service/references.service';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-references-update',
  templateUrl: './references-update.component.html',
})
export class ReferencesUpdateComponent implements OnInit {
  isSaving = false;
  references: IReferences | null = null;
  isInvalid = true;

  editForm: ReferencesFormGroup = this.referencesFormService.createReferencesFormGroup();

  constructor(
    protected referencesService: ReferencesService,
    protected referencesFormService: ReferencesFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ references }) => {
      this.references = references;
      if (references) {
        this.updateForm(references);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const references = this.referencesFormService.getReferences(this.editForm);
    if (references.id !== null) {
      this.subscribeToSaveResponse(this.referencesService.update(references));
    } else {
      this.subscribeToSaveResponse(this.referencesService.create(references as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferences>>): void {
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

  protected updateForm(references: IReferences): void {
    this.references = references;
    this.referencesFormService.resetForm(this.editForm, references);
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
