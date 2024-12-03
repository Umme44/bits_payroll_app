import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IInsuranceConfiguration } from '../insurance-configuration.model';
import { InsuranceConfigurationService } from '../service/insurance-configuration.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './insurance-configuration-delete-dialog.component.html',
})
export class InsuranceConfigurationDeleteDialogComponent {
  insuranceConfiguration?: IInsuranceConfiguration;

  constructor(
    protected insuranceConfigurationService: InsuranceConfigurationService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insuranceConfigurationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('insuranceConfigurationListModification');
      this.activeModal.close();
    });
  }
}
