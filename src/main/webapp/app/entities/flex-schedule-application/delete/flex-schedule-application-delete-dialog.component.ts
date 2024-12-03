import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlexScheduleApplication } from '../flex-schedule-application.model';
import { FlexScheduleApplicationService } from '../service/flex-schedule-application.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './flex-schedule-application-delete-dialog.component.html',
})
export class FlexScheduleApplicationDeleteDialogComponent {
  flexScheduleApplication?: IFlexScheduleApplication;

  constructor(protected flexScheduleApplicationService: FlexScheduleApplicationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flexScheduleApplicationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
