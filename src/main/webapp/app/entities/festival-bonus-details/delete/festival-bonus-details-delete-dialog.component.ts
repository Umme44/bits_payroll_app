import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import { FestivalBonusDetailsService } from '../service/festival-bonus-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './festival-bonus-details-delete-dialog.component.html',
})
export class FestivalBonusDetailsDeleteDialogComponent {
  festivalBonusDetails?: IFestivalBonusDetails;

  constructor(protected festivalBonusDetailsService: FestivalBonusDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.festivalBonusDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
