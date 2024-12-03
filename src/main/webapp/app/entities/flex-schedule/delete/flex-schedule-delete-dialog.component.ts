import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlexSchedule } from '../flex-schedule.model';
import { FlexScheduleService } from '../service/flex-schedule.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './flex-schedule-delete-dialog.component.html',
})
export class FlexScheduleDeleteDialogComponent {
  flexSchedule?: IFlexSchedule;

  constructor(protected flexScheduleService: FlexScheduleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flexScheduleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
