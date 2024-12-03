import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './unit-of-measurement-delete-dialog.component.html',
})
export class UnitOfMeasurementDeleteDialogComponent {
  unitOfMeasurement?: IUnitOfMeasurement;

  constructor(
    protected unitOfMeasurementService: UnitOfMeasurementService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.unitOfMeasurementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('unitOfMeasurementListModification');
      this.activeModal.close();
    });
  }
}
