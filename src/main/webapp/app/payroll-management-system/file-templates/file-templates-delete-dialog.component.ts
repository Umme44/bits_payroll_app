import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FileTemplatesService } from './file-templates.service';
import { IFileTemplates } from '../../shared/legacy/legacy-model/file-templates.model';

@Component({
  selector: 'jhi-file-templates-delete-dialog',
  templateUrl: './file-templates-delete-dialog.component.html',
})
export class FileTemplatesDeleteDialogComponent {
  fileTemplates?: IFileTemplates;

  constructor(protected fileTemplatesService: FileTemplatesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileTemplatesService.delete(id).subscribe(() => {
      // this.eventManager.broadcast('fileTemplatesListModification');
      this.activeModal.close();
    });
  }
}
