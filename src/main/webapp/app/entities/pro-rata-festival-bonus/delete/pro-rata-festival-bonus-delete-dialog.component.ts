import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import { ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './pro-rata-festival-bonus-delete-dialog.component.html',
})
export class ProRataFestivalBonusDeleteDialogComponent {
  proRataFestivalBonus?: IProRataFestivalBonus;

  constructor(
    protected proRataFestivalBonusService: ProRataFestivalBonusService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proRataFestivalBonusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proRataFestivalBonusListModification');
      this.activeModal.close();
    });
  }
}
