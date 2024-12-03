import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAitConfig } from '../ait-config.model';
import { AitConfigService } from '../service/ait-config.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ait-config-delete-dialog.component.html',
})
export class AitConfigDeleteDialogComponent {
  aitConfig?: IAitConfig;

  constructor(protected aitConfigService: AitConfigService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aitConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
