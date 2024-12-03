import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileTemplates } from '../file-templates.model';
import { FileTemplatesService } from '../service/file-templates.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './file-templates-delete-dialog.component.html',
})
export class FileTemplatesDeleteDialogComponent {
  fileTemplates?: IFileTemplates;

  constructor(protected fileTemplatesService: FileTemplatesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileTemplatesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
