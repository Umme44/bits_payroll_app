import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IInsuranceClaim} from "../insurance-claim.model";
import {EventManager} from "../../../core/util/event-manager.service";
import {InsuranceClaimService} from "../service/insurance-claim.service";


@Component({
  templateUrl: './insurance-claim-delete-dialog.component.html',
})
export class InsuranceClaimDeleteDialogComponent {
  insuranceClaim?: IInsuranceClaim;

  constructor(
    protected insuranceClaimService: InsuranceClaimService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insuranceClaimService.delete(id).subscribe(() => {
      this.eventManager.broadcast('insuranceClaimListModification');
      this.activeModal.close();
    });
  }
}
