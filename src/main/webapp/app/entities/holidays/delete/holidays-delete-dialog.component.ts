import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHolidays } from '../holidays.model';
import { HolidaysService } from '../service/holidays.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './holidays-delete-dialog.component.html',
})
export class HolidaysDeleteDialogComponent {
  holidays?: IHolidays;

  constructor(protected holidaysService: HolidaysService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.holidaysService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
