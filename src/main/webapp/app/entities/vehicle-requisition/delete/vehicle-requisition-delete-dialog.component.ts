import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehicleRequisition } from '../vehicle-requisition.model';
import { VehicleRequisitionService } from '../service/vehicle-requisition.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './vehicle-requisition-delete-dialog.component.html',
})
export class VehicleRequisitionDeleteDialogComponent {
  vehicleRequisition?: IVehicleRequisition;

  constructor(protected vehicleRequisitionService: VehicleRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
