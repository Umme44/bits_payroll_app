import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfNominee } from '../pf-nominee.model';
import { PfNomineeService } from '../service/pf-nominee.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-nominee-delete-dialog.component.html',
})
export class PfNomineeDeleteDialogComponent {
  pfNominee?: IPfNominee;

  constructor(protected pfNomineeService: PfNomineeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfNomineeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
