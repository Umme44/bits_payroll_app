import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFestivalBonusConfig } from '../festival-bonus-config.model';
import { FestivalBonusConfigService } from '../service/festival-bonus-config.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './festival-bonus-config-delete-dialog.component.html',
})
export class FestivalBonusConfigDeleteDialogComponent {
  festivalBonusConfig?: IFestivalBonusConfig;

  constructor(protected festivalBonusConfigService: FestivalBonusConfigService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.festivalBonusConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
