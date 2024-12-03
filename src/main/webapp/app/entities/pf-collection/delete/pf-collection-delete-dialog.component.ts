import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfCollection } from '../pf-collection.model';
import { PfCollectionService } from '../service/pf-collection.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-collection-delete-dialog.component.html',
})
export class PfCollectionDeleteDialogComponent {
  pfCollection?: IPfCollection;

  constructor(protected pfCollectionService: PfCollectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfCollectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
