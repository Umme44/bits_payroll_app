import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IUnitOfMeasurement } from '../unit-of-measurement.model';

@Component({
  selector: 'jhi-unit-of-measurement-detail',
  templateUrl: './unit-of-measurement-detail.component.html',
})
export class UnitOfMeasurementDetailComponent implements OnInit {
  unitOfMeasurement: IUnitOfMeasurement | null = null;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    //this.activatedRoute.data.subscribe(({ unitOfMeasurement }) => (this.unitOfMeasurement = unitOfMeasurement));
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
