import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FileTemplatesFormGroup, FileTemplatesFormService } from './file-templates-form.service';
import { IFileTemplates } from '../file-templates.model';
import { FileTemplatesService } from '../service/file-templates.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { FileTemplatesType } from 'app/entities/enumerations/file-templates-type.model';
import { FileAccessPrevilage } from 'app/entities/enumerations/file-access-previlage.model';
import { swalOnLoading } from '../../../shared/swal-common/swal-common';
import Swal from 'sweetalert2';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TITLE,
  SWAL_SAVED_TEXT,
} from '../../../shared/swal-common/swal.properties.constant';

@Component({
  selector: 'jhi-file-templates-update',
  templateUrl: './file-templates-update.component.html',
})
export class FileTemplatesUpdateComponent implements OnInit {
  isSaving = false;
  isFileSelected = false;
  inValidFile = false;
  inValidFileErrorMsg = '';

  fileTemplates: IFileTemplates | null = null;
  fileTemplatesTypeValues = Object.keys(FileTemplatesType);
  fileAccessPrevilageValues = Object.keys(FileAccessPrevilage);

  // this InputVar is a reference to our input.
  InputVar!: ElementRef;

  // file
  selectedFileToUpload?: File;

  editForm: FileTemplatesFormGroup = this.fileTemplatesFormService.createFileTemplatesFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileTemplatesService: FileTemplatesService,
    protected fileTemplatesFormService: FileTemplatesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileTemplates }) => {
      this.fileTemplates = fileTemplates;
      if (fileTemplates) {
        this.updateForm(fileTemplates);
        this.updateFileInputValidation();
      }
    });
  }

  updateFileInputValidation(): void {
    // remove file validation in update mode
    if (this.editForm.get('id')!.value) {
      this.editForm.get('file')!.clearValidators();
      this.editForm.get('file')!.updateValueAndValidity();
    }
  }

  protected updateForm(fileTemplates: IFileTemplates): void {
    this.fileTemplates = fileTemplates;
    this.fileTemplatesFormService.resetForm(this.editForm, fileTemplates);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  fileSize(base64String: string): string {
    let n = 0;
    if (base64String.endsWith('==')) {
      n = 2;
    } else if (base64String.endsWith('=')) {
      n = 1;
    } else {
      n = 0;
    }

    const x = base64String.length * (3 / 4) - n;

    let result = '';

    if (x / 1048576 >= 1) {
      result = Math.round(x / 1048576).toFixed(3) + 'MB';
      return result;
    } else if (x / 1024 >= 1) {
      result = Math.round(x / 1024).toFixed(3) + 'KB';
      return result;
    } else {
      result = x + 'Bytes';
      return result;
    }
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.isFileSelected = true;
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      this.selectedFileToUpload = event.target.files[0];
      const sizeInKB = Number(this.selectedFileToUpload!.size / 1024);
      if (sizeInKB > 15360) {
        this.inValidFile = true;
        this.selectedFileToUpload = undefined;
        this.inValidFileErrorMsg = 'Max allowed file size is 15MB';
        this.editForm.patchValue({
          file: undefined,
        });
        return;
      } else {
        this.editForm.get('file')!.touched;
        this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: FileLoadError) => {
          this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key }));
        });
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    swalOnLoading('Saving...');
    this.isSaving = true;
    const fileTemplates = this.fileTemplatesFormService.getFileTemplates(this.editForm);
    if (fileTemplates.id) {
      if (this.isFileSelected) {
        this.subscribeToSaveResponse(this.fileTemplatesService.updateWithFile(this.selectedFileToUpload!, fileTemplates));
        this.isFileSelected = false;
      } else {
        this.subscribeToSaveResponse(this.fileTemplatesService.updateWithoutFile(fileTemplates));
      }
    } else {
      this.subscribeToSaveResponse(this.fileTemplatesService.createWithFile(this.selectedFileToUpload!, fileTemplates));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileTemplates>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.isSaving) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: SWAL_SAVED_TEXT,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_REJECTED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.isSaving = false;
    setTimeout(() => {
      this.previousState();
    }, 2000);
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      title: SWAL_RESPONSE_ERROR_TITLE,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
    this.isSaving = false;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
