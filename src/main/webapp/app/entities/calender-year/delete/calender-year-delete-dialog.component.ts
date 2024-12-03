import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICalenderYear } from '../calender-year.model';
import { CalenderYearService } from '../service/calender-year.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './calender-year-delete-dialog.component.html',
})
export class CalenderYearDeleteDialogComponent {
  calenderYear?: ICalenderYear;

  constructor(protected calenderYearService: CalenderYearService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calenderYearService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
