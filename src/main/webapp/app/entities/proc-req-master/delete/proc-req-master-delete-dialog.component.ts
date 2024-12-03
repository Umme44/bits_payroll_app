import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProcReqMaster } from '../proc-req-master.model';
import { ProcReqMasterService } from '../service/proc-req-master.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './proc-req-master-delete-dialog.component.html',
})
export class ProcReqMasterDeleteDialogComponent {
  procReqMaster?: IProcReqMaster;

  constructor(protected procReqMasterService: ProcReqMasterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.procReqMasterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
