import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProcReq } from '../proc-req.model';
import { ProcReqService } from '../service/proc-req.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './proc-req-delete-dialog.component.html',
})
export class ProcReqDeleteDialogComponent {
  procReq?: IProcReq;

  constructor(protected procReqService: ProcReqService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.procReqService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
