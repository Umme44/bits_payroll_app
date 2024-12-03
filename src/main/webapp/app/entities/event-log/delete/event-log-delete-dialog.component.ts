import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './event-log-delete-dialog.component.html',
})
export class EventLogDeleteDialogComponent {
  eventLog?: IEventLog;

  constructor(protected eventLogService: EventLogService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
