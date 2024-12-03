import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './insurance-registration-delete-dialog.component.html',
})
export class InsuranceRegistrationDeleteDialogComponent {
  insuranceRegistration?: IInsuranceRegistration;

  constructor(
    protected insuranceRegistrationService: InsuranceRegistrationService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insuranceRegistrationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('insuranceRegistrationListModification');
      this.activeModal.close();
    });
  }
}
