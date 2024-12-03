import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { swalOnRequestError, swalOnSavedSuccess, swalOnUpdatedSuccess } from 'app/shared/swal-common/swal-common';
import { IEmployee } from '../../employee-custom.model';
import { ReferencesService } from '../../../references/service/references.service';
import { EmployeeCustomService } from '../../service/employee-custom.service';
import { ReferencesFormService } from '../../../references/update/references-form.service';
import { IReferences } from '../../../references/references.model';

@Component({
  selector: 'jhi-employee-references-update',
  templateUrl: './employee-references-update.component.html',
})
export class EmployeeReferencesUpdateComponent implements OnInit, OnChanges {
  @Input()
  idForUpdate = -1;

  @Output()
  idForUpdateChange = new EventEmitter<number>();

  @Input()
  employeeId!: number;

  isSaving = false;
  employees: IEmployee[] = [];

  editForm = this.referencesFormService.createReferencesFormGroup();

  constructor(
    protected referencesService: ReferencesService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private referencesFormService: ReferencesFormService
  ) {}

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.idForUpdate !== -1) {
      this.referencesService.find(this.idForUpdate).subscribe(
        res => {
          this.updateForm(res.body!);
        },
        () => {
          swalOnRequestError();
        }
      );
    }
  }

  updateForm(references: IReferences): void {
    this.referencesFormService.resetForm(this.editForm, references);
  }

  clear(): void {
    this.editForm.reset();
    //change to -1 for two-way data bind and activating ngOnChange
    this.idForUpdateChange.emit(-1);
  }

  save(): void {
    this.isSaving = true;
    const references = this.referencesFormService.getReferences(this.editForm);
    if (references.id !== null && references.id !== undefined) {
      this.subscribeToSaveResponse(this.referencesService.update(references));
    } else {
      this.subscribeToSaveResponse(this.referencesService.create(references));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferences>>): void {
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
    this.isSaving = false;
    this.clear();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }
}
