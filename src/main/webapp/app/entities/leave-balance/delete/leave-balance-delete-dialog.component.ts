import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {ILeaveBalance} from "../leave-balance.model";
import {LeaveBalanceService} from "../service/leave-balance.service";
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  templateUrl: './leave-balance-delete-dialog.component.html',
})
export class LeaveBalanceDeleteDialogComponent {
  leaveBalance?: ILeaveBalance;

  constructor(
    protected leaveBalanceService: LeaveBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaveBalanceListModification');
      this.activeModal.close();
    });
  }
}
