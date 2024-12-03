import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaveAllocation } from '../leave-allocation.model';
import { LeaveAllocationService } from '../service/leave-allocation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './leave-allocation-delete-dialog.component.html',
})
export class LeaveAllocationDeleteDialogComponent {
  leaveAllocation?: ILeaveAllocation;

  constructor(protected leaveAllocationService: LeaveAllocationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveAllocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
