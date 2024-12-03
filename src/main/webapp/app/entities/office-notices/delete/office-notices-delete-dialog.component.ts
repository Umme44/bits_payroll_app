import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOfficeNotices } from '../office-notices.model';
import { OfficeNoticesService } from '../service/office-notices.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './office-notices-delete-dialog.component.html',
})
export class OfficeNoticesDeleteDialogComponent {
  officeNotices?: IOfficeNotices;

  constructor(protected officeNoticesService: OfficeNoticesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.officeNoticesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
