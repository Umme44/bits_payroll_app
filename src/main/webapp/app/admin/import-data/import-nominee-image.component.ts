import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { swalOnLoading, swalOnSavedSuccess } from '../../shared/swal-common/swal-common';
import { ImportDataService } from './import-data.service';

@Component({
  selector: 'jhi-import-nominee-image',
  templateUrl: './import-nominee-image.component.html',
})
export class ImportNomineeImageComponent implements OnInit, OnDestroy {
  selectedFiles: File[] = [];
  selectedPreviewFiles: string[] = [];
  isSaving!: Boolean;
  fileValidationMessage = '';

  constructor(protected importDataService: ImportDataService, protected router: Router) {}

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
      swalOnLoading('Uploading...');
      this.importDataService.uploadNomineeImage(this.selectedFiles).subscribe(() => {
        swalOnSavedSuccess();
      });
    } else {
      this.fileValidationMessage = 'No File Selected';
    }
  }
}
