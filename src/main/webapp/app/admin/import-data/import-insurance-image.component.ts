import { Component, OnInit } from '@angular/core';
import { swalOnLoading, swalOnSavedSuccess } from 'app/shared/swal-common/swal-common';
import { ImportDataService } from 'app/admin/import-data/import-data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-import-insurance-image',
  templateUrl: './import-insurance-image.component.html',
})
export class ImportInsuranceImageComponent implements OnInit {
  selectedFiles: File[] = [];
  selectedPreviewFiles: string[] = [];
  isSaving!: Boolean;
  fileValidationMessage = '';

  constructor(protected importDataService: ImportDataService, protected router: Router) {}

  ngOnInit(): void {}

  onChange(event: any): void {
    this.selectedFiles = [];
    this.selectedPreviewFiles = [];
    this.selectedFiles = event.target.files;
  }

  onSubmit(): void {
    this.fileValidationMessage = '';
    if (this.selectedFiles.length > 0) {
      swalOnLoading('Uploading...');
      this.importDataService.uploadInsuranceImage(this.selectedFiles).subscribe(() => {
        swalOnSavedSuccess();
      });
    } else {
      this.fileValidationMessage = 'No File Selected';
    }
  }
}
