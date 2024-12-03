import { Component, OnDestroy, OnInit } from '@angular/core';

import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { swalClose, swalOnLoading, swalOnSavedSuccess } from '../../../shared/swal-common/swal-common';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';

@Component({
  selector: 'jhi-employee-static-file',
  templateUrl: './employee-id-card-upload.component.html',
})
export class EmployeeIdCardUploadComponent implements OnInit, OnDestroy {
  selectedFiles: File[] = [];
  selectedPreviewFiles: string[] = [];
  isSaving!: Boolean;
  fileValidationMessage = '';

  constructor(protected employeeStaticFileService: EmployeeStaticFileService, protected router: Router) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  onChange(event: any): void {
    this.selectedFiles = [];
    this.selectedPreviewFiles = [];
    this.selectedFiles = event.target.files;
  }

  onSubmit(): void {
    this.fileValidationMessage = '';
    if (this.selectedFiles.length > 0) {
      swalOnLoading('Uploading Id Card');
      this.employeeStaticFileService.uploadImageFiles(this.selectedFiles).subscribe(() => {
        swalClose();
        swalOnSavedSuccess();
        this.router.navigate(['/employee-static-file/id-card-list']);
      });
    } else {
      this.fileValidationMessage = 'No File Selected';
    }
  }

  removeFile(i: number): void {
    const index = this.selectedPreviewFiles.indexOf(this.selectedPreviewFiles[i]);
    this.selectedPreviewFiles.splice(index, 1);
  }

  onClear(event: any): void {
    this.selectedFiles = [];
    this.selectedPreviewFiles = [];
    event.target.files = null;
  }

  back(): void {
    window.history.back();
  }
}
